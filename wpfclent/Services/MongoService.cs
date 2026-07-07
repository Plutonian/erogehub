using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

public class MongoService
{
    private readonly IMongoCollection<BsonDocument> _collection;
    private readonly string _imgServer;

    public MongoService(string connectionString, string databaseName, string imgServer)
    {
        var client = new MongoClient(connectionString);
        _collection = client.GetDatabase(databaseName).GetCollection<BsonDocument>("game");
        _imgServer = imgServer;
    }

    private static FilterDefinition<BsonDocument> ToFilterDef(BsonDocument? filter)
        => filter == null || filter.ElementCount == 0 ? Builders<BsonDocument>.Filter.Empty : filter;

    private static BsonDocument ToMatchDoc(BsonDocument? filter) => filter ?? new BsonDocument();

    private static int SafeInt(BsonValue v) => v is null or BsonNull ? 0 : v.AsInt32;
    private static string SafeStr(BsonValue v) => v is null or BsonNull ? "" : v.AsString;
    private static bool SafeBool(BsonValue v) => v is BsonBoolean b && b.Value;

    public async Task<List<Game>> QueryGamesAsync(BsonDocument? filter, int page, int pageSize)
    {
        var sort = new BsonDocument { { "publishDate", -1 }, { "name", -1 } };
        var docs = await _collection.Find(ToFilterDef(filter))
            .Sort(sort).Skip((page - 1) * pageSize).Limit(pageSize).ToListAsync();
        return docs.Select(MapToGame).ToList();
    }

    public async Task<long> CountAsync(BsonDocument? filter)
        => await _collection.CountDocumentsAsync(ToFilterDef(filter));

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

    public async Task<List<DateGroupItem>> GroupByDateAsync(BsonDocument? filter)
    {
        var docs = await _collection.Find(ToFilterDef(filter)).Project(Builders<BsonDocument>.Projection.Include("publishDate")).ToListAsync();
        var dates = docs.Where(d => d.Contains("publishDate") && d["publishDate"].BsonType == BsonType.DateTime)
            .Select(d => d["publishDate"].AsBsonDateTime.ToUniversalTime().ToLocalTime()).Where(dt => dt.Year > 0).ToList();

        return dates.GroupBy(dt => dt.Year).Where(g => g.Key > 0).OrderByDescending(g => g.Key).Select(yearGroup => new DateGroupItem
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
    }

    public async Task<List<BrandGroupItem>> GroupByBrandAsync(BsonDocument? filter)
    {
        var docs = await _collection.Find(ToFilterDef(filter)).Project(Builders<BsonDocument>.Projection.Include("brand")).ToListAsync();
        var brands = docs.Where(d => d.Contains("brand") && d["brand"].IsBsonDocument).Select(d => d["brand"].AsBsonDocument).ToList();
        var result = new List<BrandGroupItem>();

        var withComp = brands.Where(b => b.Contains("comp") && !string.IsNullOrEmpty(SafeStr(b["comp"]))).GroupBy(b => SafeStr(b["comp"]));
        foreach (var cg in withComp)
        {
            var nodes = cg.GroupBy(b => b.Contains("name") ? SafeStr(b["name"]) : "")
                .Select(bg => new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = ExtractBrand(bg.First()) })
                .OrderByDescending(b => b.Count).ToList();
            if (nodes.Count == 1) result.Add(nodes[0]);
            else result.Add(new BrandGroupItem { Title = cg.Key, Count = cg.Count(), Comp = cg.Key, Children = new(nodes) });
        }

        var withoutComp = brands.Where(b => !b.Contains("comp") || string.IsNullOrEmpty(SafeStr(b["comp"]))).GroupBy(b => b.Contains("name") ? SafeStr(b["name"]) : "");
        foreach (var bg in withoutComp)
            result.Add(new BrandGroupItem { Title = bg.Key, Count = bg.Count(), Brand = ExtractBrand(bg.First()) });

        return result.OrderByDescending(r => r.Count).ToList();
    }

    public async Task<Game?> QueryGameByIdAsync(int id)
    {
        var doc = await _collection.Find(Builders<BsonDocument>.Filter.Eq("_id", id)).FirstOrDefaultAsync();
        return doc == null ? null : MapToGame(doc);
    }

    public async Task<bool> UpdateEmotionAsync(int id, int emotion)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("emotion", emotion))).ModifiedCount > 0;

    public async Task<bool> UpdatePlayStateAsync(int id, int playState)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("playState", playState))).ModifiedCount > 0;

    public async Task<bool> UpdateStarAsync(int id, int star)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("star", star))).ModifiedCount > 0;

    public async Task<bool> UpdateLocationAsync(int id, int location)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("location", location))).ModifiedCount > 0;

    public async Task<bool> MarkSameAsync(int id, bool isSame)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("isSame", isSame))).ModifiedCount > 0;

    public async Task<bool> UpdateTagsAsync(int id, List<string> tags)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("tag", new BsonArray(tags)))).ModifiedCount > 0;

    public async Task<bool> UpdateTitleAsync(int id, string mainTitle, string subTitle)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.Eq("_id", id),
            Builders<BsonDocument>.Update.Set("mainTitle", mainTitle).Set("subTitle", subTitle))).ModifiedCount > 0;

    public async Task<bool> SetCharCVAsync(int id, int charIndex, string cv)
        => (await _collection.UpdateOneAsync(
            Builders<BsonDocument>.Filter.And(
                Builders<BsonDocument>.Filter.Eq("_id", id),
                Builders<BsonDocument>.Filter.Eq("gamechar.index", charIndex)),
            Builders<BsonDocument>.Update.Set("gamechar.$.cv", cv))).ModifiedCount > 0;

    public async Task<bool> DeleteGameAsync(int id)
        => (await _collection.DeleteOneAsync(Builders<BsonDocument>.Filter.Eq("_id", id))).DeletedCount > 0;

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
            game.PublishDate = doc["publishDate"].AsBsonDateTime.ToUniversalTime().ToLocalTime();

        game.ImgBase = $"http://{_imgServer}/img/{game.PublishDate.Year}/{game.PublishDate.Month}/{game.Id}/";
        game.CoverUrl = $"{game.ImgBase}game_s.jpg";
        game.StoryImageUrl = $"{game.ImgBase}game_l.jpg";
        foreach (var c in game.GameCharacters)
            c.AvatarUrl = $"{game.ImgBase}char_s_{c.Index}.jpg";
        foreach (var img in game.SimpleImgs)
            img.Url = $"{game.ImgBase}simple_l_{img.Index}.jpg";

        return game;
    }

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
                Heroine = doc.Contains("heroine") && SafeBool(doc["heroine"]),
            });
        }
        return result;
    }

    private static Brand ExtractBrand(BsonDocument doc) => new()
    {
        Id = doc.Contains("id") ? SafeInt(doc["id"]) : 0,
        Name = doc.Contains("name") ? SafeStr(doc["name"]) : "",
        Website = doc.Contains("website") ? SafeStr(doc["website"]) : "",
        Comp = doc.Contains("comp") ? SafeStr(doc["comp"]) : "",
    };

    private static string GetEmotionName(int emotion) => emotion switch
    {
        (int)Emotion.HATE => "嫌い", (int)Emotion.NORMAL => "...", (int)Emotion.HOPE => "気になり", (int)Emotion.LIKE => "好き", _ => emotion.ToString(),
    };

    private static string GetPlayStateName(int state) => state switch
    {
        (int)PlayState.NOT_PLAY => "...", (int)PlayState.PLAYING => "進行中", (int)PlayState.PLAYED => "プレイ済み", _ => state.ToString(),
    };
}
