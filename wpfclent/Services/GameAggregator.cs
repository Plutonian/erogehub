using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 游戏聚合分析：9 个 GroupBy 方法，用于右侧聚合面板和分组模式。
/// 标量分组（Emotion/Location/PlayState/Star）走 $group 管道；
/// 数组分组（CV/TrueCV/Tag）走 $unwind + $group；
/// 日期/品牌分组在内存中处理（因为需要层级结构）。
/// </summary>
public class GameAggregator
{
    private readonly MongoContext _context;

    public GameAggregator(MongoContext context)
    {
        _context = context;
    }

    // ==================== 标量分组 ====================

    /// <summary>按情感值分组聚合</summary>
    public async Task<List<EmotionGroupItem>> GroupByEmotionAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$emotion" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "emotion", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var e = MongoContext.SafeInt(d.GetValue("emotion")); return new EmotionGroupItem { Emotion = e, Count = MongoContext.SafeInt(d["count"]), Title = ((Emotion)e).Label() }; }).ToList();
    }

    /// <summary>按存储位置分组聚合</summary>
    public async Task<List<LocationGroupItem>> GroupByLocationAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$location" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "location", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var l = MongoContext.SafeInt(d.GetValue("location")); return new LocationGroupItem { Location = l, Count = MongoContext.SafeInt(d["count"]), Title = ((Location)l).ToString() }; }).ToList();
    }

    /// <summary>按游玩状态分组聚合</summary>
    public async Task<List<PlayStateGroupItem>> GroupByPlayStateAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$playState" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", -1)),
            new BsonDocument("$project", new BsonDocument { { "playState", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var p = MongoContext.SafeInt(d.GetValue("playState")); return new PlayStateGroupItem { PlayState = p, Count = MongoContext.SafeInt(d["count"]), Title = ((PlayState)p).Label() }; }).ToList();
    }

    /// <summary>按星级分组聚合</summary>
    public async Task<List<StarGroupItem>> GroupByStarAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$group", new BsonDocument { { "_id", "$star" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("_id", 1)),
            new BsonDocument("$project", new BsonDocument { { "star", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => { var s = MongoContext.SafeInt(d.GetValue("star")); return new StarGroupItem { Star = s, Count = MongoContext.SafeInt(d["count"]), Title = $"{s}★" }; }).ToList();
    }

    // ==================== 数组分组 ====================

    /// <summary>按声优（马甲名）分组聚合，展开 gamechar 数组统计</summary>
    public async Task<List<GroupItem>> GroupByCVAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$gamechar"),
            new BsonDocument("$match", new BsonDocument("$and", new BsonArray { new BsonDocument("gamechar.cv", new BsonDocument("$ne", BsonNull.Value)), new BsonDocument("gamechar.cv", new BsonDocument("$ne", "")) })),
            new BsonDocument("$group", new BsonDocument { { "_id", "$gamechar.cv" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = MongoContext.SafeStr(d.GetValue("title")), Count = MongoContext.SafeInt(d["count"]) }).ToList();
    }

    /// <summary>按去马甲CV（真实声优名）分组聚合</summary>
    public async Task<List<GroupItem>> GroupByTrueCVAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$gamechar"),
            new BsonDocument("$match", new BsonDocument("$and", new BsonArray { new BsonDocument("gamechar.truecv", new BsonDocument("$ne", BsonNull.Value)), new BsonDocument("gamechar.truecv", new BsonDocument("$ne", "")) })),
            new BsonDocument("$group", new BsonDocument { { "_id", "$gamechar.truecv" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = MongoContext.SafeStr(d.GetValue("title")), Count = MongoContext.SafeInt(d["count"]) }).ToList();
    }

    /// <summary>按标签分组聚合，展开 tag 数组统计</summary>
    public async Task<List<GroupItem>> GroupByTagAsync(FilterDefinition<Game>? filter)
    {
        var pipeline = new[]
        {
            new BsonDocument("$match", _context.ToMatchDoc(filter)),
            new BsonDocument("$unwind", "$tag"),
            new BsonDocument("$match", new BsonDocument("tag", new BsonDocument("$ne", ""))),
            new BsonDocument("$group", new BsonDocument { { "_id", "$tag" }, { "count", new BsonDocument("$sum", 1) } }),
            new BsonDocument("$sort", new BsonDocument("count", -1)),
            new BsonDocument("$project", new BsonDocument { { "title", "$_id" }, { "count", 1 }, { "_id", 0 } }),
        };
        var docs = await _context.Games.Aggregate<BsonDocument>(pipeline).ToListAsync();
        return docs.Select(d => new GroupItem { Title = MongoContext.SafeStr(d.GetValue("title")), Count = MongoContext.SafeInt(d["count"]) }).ToList();
    }

    // ==================== 内存分组（层级结构） ====================

    /// <summary>按发布日期分组聚合（年 → 月层级），在内存中处理。日期为空或无效的游戏归入"无日期"组。</summary>
    public async Task<List<DateGroupItem>> GroupByDateAsync(FilterDefinition<Game>? filter)
    {
        // 用强类型投影只取 PublishDate，减少网络传输
        var docs = await _context.Games.Find(MongoContext.ToGameFilter(filter))
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
    public async Task<List<BrandGroupItem>> GroupByBrandAsync(FilterDefinition<Game>? filter)
    {
        // 用强类型投影只取 Brand，driver 自动反序列化
        var brands = await _context.Games.Find(MongoContext.ToGameFilter(filter))
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
}
