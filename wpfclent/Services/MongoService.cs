using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// MongoDB 数据服务，封装所有数据库读写操作
/// 包括：游戏查询、聚合分析、字段更新、数据映射
/// </summary>
public class MongoService
{
    /// <summary>MongoDB game 集合</summary>
    private readonly IMongoCollection<BsonDocument> _collection;

    /// <summary>图片服务器地址（网络模式）</summary>
    private readonly string _imgServer;

    /// <summary>是否使用本地图片路径</summary>
    private readonly bool _useLocalImg;

    /// <summary>本地图片根目录（本地模式）</summary>
    private readonly string _localImgPath;

    public MongoService(string connectionString, string databaseName, string imgServer, bool useLocalImg, string localImgPath)
    {
        var client = new MongoClient(connectionString);
        _collection = client.GetDatabase(databaseName).GetCollection<BsonDocument>("game");
        _imgServer = imgServer;
        _useLocalImg = useLocalImg;
        _localImgPath = localImgPath;
    }

    /// <summary>将 BsonDocument 过滤器转为标准的 FilterDefinition，空过滤器返回 Empty</summary>
    private static FilterDefinition<BsonDocument> ToFilterDef(BsonDocument? filter)
        => filter == null || filter.ElementCount == 0 ? Builders<BsonDocument>.Filter.Empty : filter;

    /// <summary>空安全获取 $match 阶段的文档</summary>
    private static BsonDocument ToMatchDoc(BsonDocument? filter) => filter ?? new BsonDocument();

    /// <summary>安全的 int 取值，null 返回 0</summary>
    private static int SafeInt(BsonValue v) => v is null or BsonNull ? 0 : v.AsInt32;

    /// <summary>安全的 string 取值，null 返回空串</summary>
    private static string SafeStr(BsonValue v) => v is null or BsonNull ? "" : v.AsString;

    /// <summary>安全的 bool 取值，非布尔类型返回 false</summary>
    private static bool SafeBool(BsonValue v) => v is BsonBoolean b && b.Value;

    /// <summary>
    /// 分页查询游戏列表，按发布日期和名称降序排列
    /// </summary>
    public async Task<List<Game>> QueryGamesAsync(BsonDocument? filter, int page, int pageSize)
    {
        var sort = new BsonDocument { { "publishDate", -1 }, { "name", -1 } };
        var docs = await _collection.Find(ToFilterDef(filter))
            .Sort(sort).Skip((page - 1) * pageSize).Limit(pageSize).ToListAsync();
        return docs.Select(MapToGame).ToList();
    }

    /// <summary>查询符合条件的文档总数</summary>
    public async Task<long> CountAsync(BsonDocument? filter)
        => await _collection.CountDocumentsAsync(ToFilterDef(filter));

    /// <summary>查询全部符合条件的游戏（不分页），用于分组模式</summary>
    public async Task<List<Game>> QueryAllGamesAsync(BsonDocument? filter)
    {
        var sort = new BsonDocument { { "publishDate", -1 }, { "name", -1 } };
        var docs = await _collection.Find(ToFilterDef(filter)).Sort(sort).ToListAsync();
        return docs.Select(MapToGame).ToList();
    }

    /// <summary>按情感值分组聚合</summary>
    public async Task<List<EmotionGroupItem>> GroupByEmotionAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$emotion" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "emotion", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var e = SafeInt(d.GetValue("emotion")); return new EmotionGroupItem { Emotion = e, Count = SafeInt(d["count"]), Title = GetEmotionName(e) }; }).ToList();
    }

    /// <summary>按存储位置分组聚合</summary>
    public async Task<List<LocationGroupItem>> GroupByLocationAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$location" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "location", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var l = SafeInt(d.GetValue("location")); return new LocationGroupItem { Location = l, Count = SafeInt(d["count"]), Title = ((Location)l).ToString() }; }).ToList();
    }

    /// <summary>按游玩状态分组聚合</summary>
    public async Task<List<PlayStateGroupItem>> GroupByPlayStateAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$playState" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "playState", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var p = SafeInt(d.GetValue("playState")); return new PlayStateGroupItem { PlayState = p, Count = SafeInt(d["count"]), Title = GetPlayStateName(p) }; }).ToList();
    }

    /// <summary>按星级分组聚合</summary>
    public async Task<List<StarGroupItem>> GroupByStarAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$star" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", 1)),
            new BsonDocument("$project", new BsonDocument { { "star", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var s = SafeInt(d.GetValue("star")); return new StarGroupItem { Star = s, Count = SafeInt(d["count"]), Title = $"{s}★" }; }).ToList();
    }

    /// <summary>按声优（马甲名）分组聚合，展开 gamechar 数组统计</summary>
    public async Task<List<GroupItem>> GroupByCVAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$gamechar"),
            new BsonDocument("$match", new BsonDocument("$and", new BsonArray { new BsonDocument("gamechar.cv", new BsonDocument("$ne", BsonNull.Value)), new BsonDocument("gamechar.cv", new BsonDocument("$ne", "")) })),
            new BsonDocument("$group", new BsonDocument { { "_id", "$gamechar.cv" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = SafeStr(d.GetValue("title")), Count = SafeInt(d["count"]) }).ToList();
    }

    /// <summary>按去马甲CV（真实声优名）分组聚合</summary>
    public async Task<List<GroupItem>> GroupByTrueCVAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$gamechar"),
            new BsonDocument("$match", new BsonDocument("$and", new BsonArray { new BsonDocument("gamechar.truecv", new BsonDocument("$ne", BsonNull.Value)), new BsonDocument("gamechar.truecv", new BsonDocument("$ne", "")) })),
            new BsonDocument("$group", new BsonDocument { { "_id", "$gamechar.truecv" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = SafeStr(d.GetValue("title")), Count = SafeInt(d["count"]) }).ToList();
    }

    /// <summary>按标签分组聚合，展开 tag 数组统计</summary>
    public async Task<List<GroupItem>> GroupByTagAsync(BsonDocument? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$tag"),
            new BsonDocument("$match", new BsonDocument("tag", new BsonDocument("$ne", ""))),
            new BsonDocument("$group", new BsonDocument { { "_id", "$tag" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _collection.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = SafeStr(d.GetValue("title")), Count = SafeInt(d["count"]) }).ToList();
    }

    /// <summary>按发布日期分组聚合（年 → 月层级），在内存中处理。日期为空或无效的游戏归入"无日期"组。</summary>
    public async Task<List<DateGroupItem>> GroupByDateAsync(BsonDocument? filter)
    {
        var docs = await _collection.Find(ToFilterDef(filter)).Project(Builders<BsonDocument>.Projection.Include("publishDate")).ToListAsync();

        var validDates = new List<DateTime>();
        var noDateCount = 0;
        foreach (var d in docs)
        {
            if (d.Contains("publishDate") && d["publishDate"].BsonType == BsonType.DateTime)
            {
                try
                {
                    var dt = d["publishDate"].AsBsonDateTime.ToUniversalTime().ToLocalTime();
                    if (dt.Year > 1) validDates.Add(dt);
                    else noDateCount++;
                }
                catch { noDateCount++; }
            }
            else noDateCount++;
        }

        var result = validDates.GroupBy(dt => dt.Year).Where(g => g.Key > 0).OrderByDescending(g => g.Key).Select(yearGroup => new DateGroupItem
        {
            Title = $"{yearGroup.Key}年",
            Start = new DateTime(yearGroup.Key, 1, 1),
            End = new DateTime(yearGroup.Key, 12, 31),
            Index = yearGroup.Key,
            Count = yearGroup.Count(),
            DateType = DateType.YEAR,
            Children = new(yearGroup.GroupBy(dt => dt.Month).OrderByDescending(g => g.Key).Select(mg => new DateGroupItem
            {
                Title = $"{mg.Key}月",
                Start = new DateTime(yearGroup.Key, mg.Key, 1),
                End = new DateTime(yearGroup.Key, mg.Key, 1).AddMonths(1).AddDays(-1),
                Index = mg.Key,
                Count = mg.Count(),
                DateType = DateType.MONTH,
            })),
        }).ToList();

        // 日期为空或无效的游戏统一归入"无日期"组（无月份子节点）
        if (noDateCount > 0)
            result.Add(new DateGroupItem { Title = "无日期", Count = noDateCount, Index = 0, DateType = DateType.YEAR });

        return result;
    }

    /// <summary>按品牌分组聚合（公司 → 品牌层级），在内存中分组</summary>
    public async Task<List<BrandGroupItem>> GroupByBrandAsync(BsonDocument? filter)
    {
        var docs = await _collection.Find(ToFilterDef(filter)).Project(Builders<BsonDocument>.Projection.Include("brand")).ToListAsync();
        var result = new List<BrandGroupItem>();

        // 有品牌信息的文档
        var brands = docs.Where(d => d.Contains("brand") && d["brand"].IsBsonDocument).Select(d => d["brand"].AsBsonDocument).ToList();

        // 有公司信息的品牌：按公司分组，公司下再按品牌名分组
        var withComp = brands.Where(b => b.Contains("comp") && !string.IsNullOrEmpty(SafeStr(b["comp"]))).GroupBy(b => SafeStr(b["comp"]));
        foreach (var cg in withComp)
        {
            var nodes = cg.GroupBy(b => b.Contains("name") ? SafeStr(b["name"]) : "")
                .Select(bg => new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = ExtractBrand(bg.First()) })
                .OrderByDescending(b => b.Count).ToList();
            if (nodes.Count == 1) result.Add(nodes[0]);
            else result.Add(new BrandGroupItem { Title = cg.Key, Count = cg.Count(), Comp = cg.Key, Children = new(nodes) });
        }

        // 无公司信息的品牌：直接按品牌名分组
        var withoutComp = brands.Where(b => !b.Contains("comp") || string.IsNullOrEmpty(SafeStr(b["comp"]))).GroupBy(b => b.Contains("name") ? SafeStr(b["name"]) : "");
        foreach (var bg in withoutComp)
            result.Add(new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = ExtractBrand(bg.First()) });

        // 无品牌信息的文档统一归入"无品牌"
        var noBrandCount = docs.Count(d => !d.Contains("brand") || !d["brand"].IsBsonDocument);
        if (noBrandCount > 0)
            result.Add(new BrandGroupItem { Title = "无品牌", Count = noBrandCount });

        return result.OrderByDescending(r => r.Count).ToList();
    }

    /// <summary>根据 ID 查询单个游戏</summary>
    public async Task<Game?> QueryGameByIdAsync(int id)
    {
        var doc = await _collection.Find(Builders<BsonDocument>.Filter.Eq("_id", id)).FirstOrDefaultAsync();
        return doc == null ? null : MapToGame(doc);
    }

    /// <summary>更新游戏的情感值</summary>
    public async Task<bool> UpdateEmotionAsync(int id, int emotion)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("emotion", emotion))).ModifiedCount > 0;

    /// <summary>更新游戏的游玩状态</summary>
    public async Task<bool> UpdatePlayStateAsync(int id, int playState)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("playState", playState))).ModifiedCount > 0;

    /// <summary>更新游戏的星级评分</summary>
    public async Task<bool> UpdateStarAsync(int id, int star)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("star", star))).ModifiedCount > 0;

    /// <summary>更新游戏的存储位置</summary>
    public async Task<bool> UpdateLocationAsync(int id, int location)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("location", location))).ModifiedCount > 0;

    /// <summary>标记/取消标记游戏为重复</summary>
    public async Task<bool> MarkSameAsync(int id, bool isSame)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("isSame", isSame))).ModifiedCount > 0;

    /// <summary>更新游戏的标签列表</summary>
    public async Task<bool> UpdateTagsAsync(int id, List<string> tags)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("tag", new BsonArray(tags)))).ModifiedCount > 0;

    /// <summary>更新游戏的主标题和副标题</summary>
    public async Task<bool> UpdateTitleAsync(int id, string mainTitle, string subTitle)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("mainTitle", mainTitle).Set("subTitle", subTitle))).ModifiedCount > 0;

    /// <summary>设置指定角色的声优</summary>
    public async Task<bool> SetCharCVAsync(int id, int charIndex, string cv)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.And(
                Builders<BsonDocument>.Filter.Eq("_id", id),
                Builders<BsonDocument>.Filter.Eq("gamechar.index", charIndex)),
            Builders<BsonDocument>.Update.Set("gamechar.$.cv", cv))).ModifiedCount > 0;

    /// <summary>将角色标记为男主角（SetMan）</summary>
    public async Task<bool> SetCharManAsync(int id, int charIndex)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.And(
                Builders<BsonDocument>.Filter.Eq("_id", id),
                Builders<BsonDocument>.Filter.Eq("gamechar.index", charIndex)),
            Builders<BsonDocument>.Update.Set("gamechar.$.man", true))).ModifiedCount > 0;

    /// <summary>从 cv 集合加载所有声优数据到内存字典</summary>
    private Dictionary<string, CV>? _cvMap;
    private Dictionary<string, CV> GetCVMap()
    {
        if (_cvMap != null) return _cvMap;
        var cvCollection = _collection.Database.GetCollection<BsonDocument>("cv");
        var docs = cvCollection.Find(Builders<BsonDocument>.Filter.Empty).ToList();
        _cvMap = docs
            .Where(d => d.Contains("name"))
            .Select(d => new CV
            {
                Name = SafeStr(d["name"]),
                Star = d.Contains("star") ? SafeInt(d["star"]) : 0,
            })
            .ToDictionary(cv => cv.Name, cv => cv);
        return _cvMap;
    }

    /// <summary>删除指定游戏</summary>
    public async Task<bool> DeleteGameAsync(int id)
        => (await _collection.DeleteOneAsync(Builders<BsonDocument>.Filter.Eq("_id", id))).DeletedCount > 0;

    /// <summary>
    /// 将 BsonDocument 映射为 Game 模型，同时计算图片 URL
    /// </summary>
    private Game MapToGame(BsonDocument doc)
    {
        var game = new Game
        {
            Id = doc.Contains("_id") ? SafeInt(doc["_id"]) : 0,
            Name = doc.Contains("name") ? SafeStr(doc["name"]) : "",
            MainTitle = doc.Contains("mainTitle") ? SafeStr(doc["mainTitle"]) : "",
            SubTitle = doc.Contains("subTitle") ? SafeStr(doc["subTitle"]) : "",
            Star = doc.Contains("star") ? SafeInt(doc["star"]) : 0,
            Emotion = doc.Contains("emotion") ? SafeInt(doc["emotion"]) : 0,
            Location = doc.Contains("location") ? SafeInt(doc["location"]) : 0,
            PlayState = doc.Contains("playState") ? SafeInt(doc["playState"]) : 0,
            IsSame = doc.Contains("isSame") && SafeBool(doc["isSame"]),
            Story = doc.Contains("story") ? SafeStr(doc["story"]) : "",
            Intro = doc.Contains("intro") ? SafeStr(doc["intro"]) : "",
            Tag = doc.Contains("tag") && doc["tag"].IsBsonArray ? doc["tag"].AsBsonArray.Select(SafeStr).ToList() : new(),
            Painter = doc.Contains("painter") && doc["painter"].IsBsonArray ? doc["painter"].AsBsonArray.Select(SafeStr).ToList() : new(),
            Writer = doc.Contains("writer") && doc["writer"].IsBsonArray ? doc["writer"].AsBsonArray.Select(SafeStr).ToList() : new(),
            Brand = doc.Contains("brand") && doc["brand"].IsBsonDocument ? ExtractBrand(doc["brand"].AsBsonDocument) : null,
            GameCharacters = doc.Contains("gamechar") && doc["gamechar"].IsBsonArray ? MapCharacters(doc["gamechar"].AsBsonArray) : new(),
            SimpleImgs = doc.Contains("simpleImg") && doc["simpleImg"].IsBsonArray
                ? doc["simpleImg"].AsBsonArray.Select(v => v.IsBsonDocument
                    ? new GameImg { Index = v.AsBsonDocument.Contains("index") ? SafeInt(v.AsBsonDocument["index"]) : 0 }
                    : new GameImg { Index = SafeInt(v) }).ToList()
                : new(),
        };
        if (doc.Contains("publishDate") && doc["publishDate"].BsonType == BsonType.DateTime)
        {
            try { game.PublishDate = doc["publishDate"].AsBsonDateTime.ToUniversalTime().ToLocalTime(); }
            catch { /* 日期超出 .NET DateTime 范围，保持默认值 */ }
        }

        // 计算图片资源 URL（根据配置选择网络或本地路径）
        game.ImgBase = _useLocalImg
            ? $"file:///{_localImgPath.Replace('\\', '/').TrimEnd('/')}/img/{game.PublishDate.Year}/{game.PublishDate.Month}/{game.Id}/"
            : $"http://{_imgServer}/img/{game.PublishDate.Year}/{game.PublishDate.Month}/{game.Id}/";
        game.CoverUrl = $"{game.ImgBase}game_s.jpg";
        game.StoryImageUrl = $"{game.ImgBase}game_l.jpg";

        // 加载 cv 地图并填充角色数据
        var cvMap = GetCVMap();
        foreach (var c in game.GameCharacters)
        {
            c.AvatarUrl = $"{game.ImgBase}char_s_{c.Index}.jpg";
            // 通过 trueCV 匹配声优评分数据
            if (!string.IsNullOrEmpty(c.TrueCV) && cvMap.TryGetValue(c.TrueCV, out var cv))
                c.CvObj = cv;
            // 角色名按空格拆分为 title / subTitle
            SplitCharName(c);
        }
        foreach (var img in game.SimpleImgs)
            img.Url = $"{game.ImgBase}simple_l_{img.Index}.jpg";

        return game;
    }

    /// <summary>将角色名按空格拆分为 CharTitle 和 CharSubTitle</summary>
    private static void SplitCharName(GameCharacter c)
    {
        if (string.IsNullOrEmpty(c.Name)) return;
        var parts = c.Name.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
        if (parts.Length > 1)
        {
            c.CharSubTitle = parts[0];
            c.CharTitle = string.Join(" ", parts.Skip(1));
        }
        else
        {
            c.CharTitle = c.Name;
        }
    }

    /// <summary>将 BsonArray 角色数据映射为 GameCharacter 列表</summary>
    private static List<GameCharacter> MapCharacters(BsonArray arr)
    {
        var result = new List<GameCharacter>();
        foreach (var item in arr)
        {
            if (item is not BsonDocument doc) continue;
            result.Add(new GameCharacter
            {
                Name = doc.Contains("name") ? SafeStr(doc["name"]) : "",
                Cv = doc.Contains("cv") ? SafeStr(doc["cv"]) : "",
                Intro = doc.Contains("intro") ? SafeStr(doc["intro"]) : "",
                TrueCV = doc.Contains("truecv") ? SafeStr(doc["truecv"]) : "",
                Index = doc.Contains("index") ? SafeInt(doc["index"]) : 0,
                Man = doc.Contains("man") && SafeBool(doc["man"]),
            });
        }
        return result;
    }

    /// <summary>从 BsonDocument 提取 Brand 对象</summary>
    private static Brand ExtractBrand(BsonDocument doc) => new()
    {
        Id = doc.Contains("id") ? SafeInt(doc["id"]) : 0,
        Name = doc.Contains("name") ? SafeStr(doc["name"]) : "",
        Website = doc.Contains("website") ? SafeStr(doc["website"]) : "",
        Comp = doc.Contains("comp") ? SafeStr(doc["comp"]) : "",
    };

    /// <summary>情感数字 → 显示名称</summary>
    private static string GetEmotionName(int emotion) => emotion switch
    {
        (int)Emotion.HATE => "嫌い", (int)Emotion.NORMAL => "...", (int)Emotion.HOPE => "気になり", (int)Emotion.LIKE => "好き", _ => emotion.ToString(),
    };

    /// <summary>游玩状态数字 → 显示名称</summary>
    private static string GetPlayStateName(int state) => state switch
    {
        (int)PlayState.NOT_PLAY => "...", (int)PlayState.PLAYING => "進行中", (int)PlayState.PLAYED => "プレイ済み", _ => state.ToString(),
    };
}
