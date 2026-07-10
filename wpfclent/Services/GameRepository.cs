using ErogeHub.Wpf.Models;
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

    public GameRepository(MongoContext context, GameEnricher enricher)
    {
        _context = context;
        _enricher = enricher;
    }

    /// <summary>
    /// 分页查询游戏列表，按发布日期和名称降序排列。
    /// 查到后并行 enrich 全部 Game。
    /// </summary>
    public async Task<List<Game>> QueryGamesAsync(FilterDefinition<Game>? filter, int page, int pageSize)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _context.Games.Find(MongoContext.ToGameFilter(filter))
            .Sort(sort).Skip((page - 1) * pageSize).Limit(pageSize).ToListAsync();
        await _enricher.EnrichAsync(games);
        return games;
    }

    /// <summary>查询符合条件的文档总数</summary>
    public async Task<long> CountAsync(FilterDefinition<Game>? filter)
        => await _context.Games.CountDocumentsAsync(MongoContext.ToGameFilter(filter));

    /// <summary>查询全部符合条件的游戏（不分页），用于分组模式</summary>
    public async Task<List<Game>> QueryAllGamesAsync(FilterDefinition<Game>? filter)
    {
        var sort = Builders<Game>.Sort.Descending(g => g.PublishDate).Descending(g => g.Name);
        var games = await _context.Games.Find(MongoContext.ToGameFilter(filter)).Sort(sort).ToListAsync();
        await _enricher.EnrichAsync(games);
        return games;
    }

    /// <summary>根据 ID 查询单个游戏</summary>
    public async Task<Game?> QueryGameByIdAsync(int id)
    {
        var game = await _context.Games.Find(Builders<Game>.Filter.Eq(g => g.Id, id)).FirstOrDefaultAsync();
        if (game != null) await _enricher.EnrichAsync(game);
        return game;
    }

    /// <summary>删除指定游戏</summary>
    public async Task<bool> DeleteGameAsync(int id)
        => (await _context.Games.DeleteOneAsync(Builders<Game>.Filter.Eq(g => g.Id, id))).DeletedCount > 0;
}
