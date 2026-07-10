using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// MongoDB 数据服务，封装所有数据库读写操作。
/// 主集合使用强类型 IMongoCollection&lt;Game&gt;，由 driver 自动反序列化；
/// 聚合管道使用 BsonDocument，因为中间结果不是 Game 结构。
/// </summary>
public class MongoService
{
    /// <summary>MongoDB game 集合（强类型，driver 自动映射）</summary>
    private readonly IMongoCollection<Game> _collection;

    /// <summary>MongoDB cv 集合（强类型）</summary>
    private readonly IMongoCollection<CV> _cvCollection;

    /// <summary>图片服务器地址（网络模式）</summary>
    private readonly string _imgServer;

    /// <summary>是否使用本地图片路径</summary>
    private readonly bool _useLocalImg;

    /// <summary>本地图片根目录（本地模式）</summary>
    private readonly string _localImgPath;

    /// <summary>声优评分缓存（按 trueCV 名匹配），首次访问后驻留内存</summary>
    private Dictionary<string, CV>? _cvMap;

    public MongoService(string connectionString, string databaseName, string imgServer, bool useLocalImg, string localImgPath)
    {
        var client = new MongoClient(connectionString);
        var database = client.GetDatabase(databaseName);
        _collection = database.GetCollection<Game>("game");
        _cvCollection = database.GetCollection<CV>("cv");
        _imgServer = imgServer;
        _useLocalImg = useLocalImg;
        _localImgPath = localImgPath;
    }

    /// <summary>把 BsonDocument 过滤器适配为强类型 FilterDefinition，空过滤器返回 Empty</summary>
    private static FilterDefinition<Game> ToGameFilter(BsonDocument? filter)
        => filter == null || filter.ElementCount == 0 ? Builders<Game>.Filter.Empty : filter;

    /// <summary>空安全获取 $match 阶段的文档</summary>
    private static BsonDocument ToMatchDoc(BsonDocument? filter) => filter ?? new BsonDocument();

    /// <summary>安全的 int 取值，null 返回 0</summary>
    private static int SafeInt(BsonValue v) => v is null or BsonNull ? 0 : v.AsInt32;

    /// <summary>安全的 string 取值，null 返回空串</summary>
    private static string SafeStr(BsonValue v) => v is null or BsonNull ? "" : v.AsString;

    /// <summary>安全的 bool 取值，非布尔类型返回 false</summary>
    private static bool SafeBool(BsonValue v) => v is BsonBoolean b && b.Value;

    // ==================== 主查询 ====================

    /// <summary>
    /// 分页查询游戏列表，按发布日期和名称降序排列
    /// </summary>
    public async Task<List<Game>> QueryGamesAsync(BsonDocument? filter, int page, int pageSize)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _collection.Find(ToGameFilter(filter))
            .Sort(sort).Skip((page - 1) * pageSize).Limit(pageSize).ToListAsync();
        foreach (var g in games) await EnrichGameAsync(g);
        return games;
    }

    /// <summary>查询符合条件的文档总数</summary>
    public async Task<long> CountAsync(BsonDocument? filter)
        => await _collection.CountDocumentsAsync(ToGameFilter(filter));

    /// <summary>查询全部符合条件的游戏（不分页），用于分组模式</summary>
    public async Task<List<Game>> QueryAllGamesAsync(BsonDocument? filter)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _collection.Find(ToGameFilter(filter)).Sort(sort).ToListAsync();
        foreach (var g in games) await EnrichGameAsync(g);
        return games;
    }

    /// <summary>根据 ID 查询单个游戏</summary>
    public async Task<Game?> QueryGameByIdAsync(int id)
    {
        var game = await _collection.Find(Builders<Game>.Filter.Eq(g => g.Id, id)).FirstOrDefaultAsync();
        if (game != null) await EnrichGameAsync(game);
        return game;
    }

    // ==================== 聚合分析 ====================
    // 聚合管道返回的中间结构不是 Game，继续用 BsonDocument 处理

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
        // 用强类型投影只取 PublishDate，减少网络传输
        var docs = await _collection.Find(ToGameFilter(filter))
            .Project(g => g.PublishDate).ToListAsync();

        var validDates = new List<DateTime>();
        var noDateCount = 0;
        foreach (var dt in docs)
        {
            if (dt.Year > 1) validDates.Add(dt);
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
        // 用强类型投影只取 Brand，driver 自动反序列化
        var brands = await _collection.Find(ToGameFilter(filter))
            .Project(g => g.Brand).ToListAsync();
        var result = new List<BrandGroupItem>();

        var hasBrand = brands.Where(b => b != null).Select(b => b!).ToList();

        // 有公司信息的品牌：按公司分组，公司下再按品牌名分组
        var withComp = hasBrand.Where(b => !string.IsNullOrEmpty(b.Comp)).GroupBy(b => b.Comp);
        foreach (var cg in withComp)
        {
            var nodes = cg.GroupBy(b => b!.Name ?? "")
                .Select(bg => new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = bg.First() })
                .OrderByDescending(b => b.Count).ToList();
            if (nodes.Count == 1) result.Add(nodes[0]);
            else result.Add(new BrandGroupItem { Title = cg.Key, Count = cg.Count(), Comp = cg.Key, Children = new(nodes) });
        }

        // 无公司信息的品牌：直接按品牌名分组
        var withoutComp = hasBrand.Where(b => string.IsNullOrEmpty(b.Comp)).GroupBy(b => b.Name ?? "");
        foreach (var bg in withoutComp)
            result.Add(new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = bg.First() });

        // 无品牌信息的文档统一归入"无品牌"
        var noBrandCount = brands.Count(b => b == null);
        if (noBrandCount > 0)
            result.Add(new BrandGroupItem { Title = "无品牌", Count = noBrandCount });

        return result.OrderByDescending(r => r.Count).ToList();
    }

    // ==================== 字段更新 ====================

    /// <summary>更新游戏的情感值</summary>
    public async Task<bool> UpdateEmotionAsync(int id, int emotion)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Emotion, emotion))).ModifiedCount > 0;

    /// <summary>更新游戏的游玩状态</summary>
    public async Task<bool> UpdatePlayStateAsync(int id, int playState)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.PlayState, playState))).ModifiedCount > 0;

    /// <summary>更新游戏的星级评分</summary>
    public async Task<bool> UpdateStarAsync(int id, int star)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Star, star))).ModifiedCount > 0;

    /// <summary>更新游戏的存储位置</summary>
    public async Task<bool> UpdateLocationAsync(int id, int location)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Location, location))).ModifiedCount > 0;

    /// <summary>标记/取消标记游戏为重复</summary>
    public async Task<bool> MarkSameAsync(int id, bool isSame)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.IsSame, isSame))).ModifiedCount > 0;

    /// <summary>更新游戏的标签列表</summary>
    public async Task<bool> UpdateTagsAsync(int id, List<string> tags)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Tag, tags))).ModifiedCount > 0;

    /// <summary>更新游戏的主标题和副标题</summary>
    public async Task<bool> UpdateTitleAsync(int id, string mainTitle, string subTitle)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.MainTitle, mainTitle).Set(g => g.SubTitle, subTitle))).ModifiedCount > 0;

    /// <summary>设置指定角色的声优（用 ElemMatch 定位嵌套数组元素，位置操作符 $ 更新）</summary>
    public async Task<bool> SetCharCVAsync(int id, int charIndex, string cv)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.And(
                Builders<Game>.Filter.Eq(g => g.Id, id),
                Builders<Game>.Filter.ElemMatch(g => g.GameCharacters, c => c.Index == charIndex)),
            Builders<Game>.Update.Set("gamechar.$.cv", cv))).ModifiedCount > 0;

    /// <summary>将角色标记为男主角（SetMan）</summary>
    public async Task<bool> SetCharManAsync(int id, int charIndex)
        => (await _collection.UpdateOneAsync(
            Builders<Game>.Filter.And(
                Builders<Game>.Filter.Eq(g => g.Id, id),
                Builders<Game>.Filter.ElemMatch(g => g.GameCharacters, c => c.Index == charIndex)),
            Builders<Game>.Update.Set("gamechar.$.man", true))).ModifiedCount > 0;

    // ==================== 删除 ====================

    /// <summary>删除指定游戏</summary>
    public async Task<bool> DeleteGameAsync(int id)
        => (await _collection.DeleteOneAsync(Builders<Game>.Filter.Eq(g => g.Id, id))).DeletedCount > 0;

    // ==================== 派生字段增强 ====================

    /// <summary>
    /// 在 driver 自动反序列化的 Game 基础上，填充 MongoDB 中不存在的派生字段：
    /// 图片 URL、角色头像、声优评分关联、角色名拆分。
    /// 与"字段映射"分离，便于单独修改 URL 规则或 CV 匹配策略。
    /// </summary>
    private async Task EnrichGameAsync(Game game)
    {
        // 计算 PublishDate 时区：driver 默认按 UTC 反序列化，转回本地时间保持与原行为一致
        if (game.PublishDate != default && game.PublishDate.Kind == DateTimeKind.Utc)
            game.PublishDate = game.PublishDate.ToLocalTime();

        // 计算图片资源 URL（根据配置选择网络或本地路径）
        game.ImgBase = _useLocalImg
            ? $"file:///{_localImgPath.Replace('\\', '/').TrimEnd('/')}/img/{game.PublishDate.Year}/{game.PublishDate.Month}/{game.Id}/"
            : $"http://{_imgServer}/img/{game.PublishDate.Year}/{game.PublishDate.Month}/{game.Id}/";
        game.CoverUrl = $"{game.ImgBase}game_s.jpg";
        game.StoryImageUrl = $"{game.ImgBase}game_l.jpg";

        // 加载 CV 地图并填充角色数据
        var cvMap = await GetCVMapAsync();
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

    /// <summary>从 cv 集合加载所有声优数据到内存字典（强类型查询，首次访问后缓存）</summary>
    private async Task<Dictionary<string, CV>> GetCVMapAsync()
    {
        if (_cvMap != null) return _cvMap;
        var list = await _cvCollection.Find(Builders<CV>.Filter.Empty).ToListAsync();
        _cvMap = list
            .Where(cv => !string.IsNullOrEmpty(cv.Name))
            .ToDictionary(cv => cv.Name!, cv => cv);
        return _cvMap;
    }

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
