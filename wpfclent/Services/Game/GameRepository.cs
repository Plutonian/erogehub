using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 游戏查询仓储：分页/全量/单条查询 + 计数 + 删除。
/// 查询返回的 Game 会自动经过 GameEnricher 计算派生字段（角色头像、CV 关联、截图 URL 等）。
/// </summary>
public class GameRepository
{
    private readonly MongoContext _context;
    private readonly GameEnricher _enricher;

    /// <summary>
    /// 列表查询投影：排除详情页才需要的大字段（角色列表、截图列表、故事简介、画师、剧本），
    /// 减少网络传输和反序列化开销。单条查询（QueryGameByIdAsync）不使用投影，取完整文档。
    /// </summary>
    private static readonly ProjectionDefinition<Game, Game> ListProjection =
        new BsonDocumentProjectionDefinition<Game, Game>(new BsonDocument
        {
            { "gamechar", 0 },
            { "simpleImg", 0 },
            { "story", 0 },
            { "intro", 0 },
            { "painter", 0 },
            { "writer", 0 }
        });

    public GameRepository(MongoContext context, GameEnricher enricher)
    {
        _context = context;
        _enricher = enricher;
    }

    /// <summary>
    /// 分页查询游戏列表，按发布日期和名称降序排列。
    /// 使用投影排除详情页字段，减少数据传输量。查到后并行 enrich。
    /// </summary>
    public async Task<List<Game>> QueryGamesAsync(FilterDefinition<Game>? filter, int page, int pageSize, CancellationToken cancellationToken = default)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _context.Games.Find(MongoContext.ToGameFilter(filter))
            .Sort(sort).Skip((page - 1) * pageSize).Limit(pageSize)
            .Project(ListProjection)
            .ToListAsync(cancellationToken);
        await _enricher.EnrichAsync(games, cancellationToken);
        return games;
    }

    /// <summary>查询符合条件的文档总数</summary>
    public async Task<long> CountAsync(FilterDefinition<Game>? filter, CancellationToken cancellationToken = default)
        => await _context.Games.CountDocumentsAsync(MongoContext.ToGameFilter(filter), cancellationToken: cancellationToken);

    /// <summary>查询全部符合条件的游戏（不分页），用于分组模式</summary>
    public async Task<List<Game>> QueryAllGamesAsync(FilterDefinition<Game>? filter, CancellationToken cancellationToken = default)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _context.Games.Find(MongoContext.ToGameFilter(filter))
            .Sort(sort)
            .Project(ListProjection)
            .ToListAsync(cancellationToken);
        await _enricher.EnrichAsync(games, cancellationToken);
        return games;
    }

    /// <summary>根据 ID 查询单个游戏（完整文档，含角色列表、截图等）</summary>
    public async Task<Game?> QueryGameByIdAsync(int id, CancellationToken cancellationToken = default)
    {
        var game = await _context.Games.Find(Builders<Game>.Filter.Eq(g => g.Id, id)).FirstOrDefaultAsync(cancellationToken);
        if (game != null) await _enricher.EnrichAsync(game, cancellationToken);
        return game;
    }

    /// <summary>删除指定游戏</summary>
    public async Task<bool> DeleteGameAsync(int id, CancellationToken cancellationToken = default)
        => (await _context.Games.DeleteOneAsync(Builders<Game>.Filter.Eq(g => g.Id, id), cancellationToken)).DeletedCount > 0;
}
