using ErogeHub.Wpf.Models;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 游戏字段更新：单字段 UpdateOneAsync，按 ID 定位。
/// 嵌套数组元素用 ElemMatch + 位置操作符 $ 更新。
/// </summary>
public class GameUpdater
{
    private readonly MongoContext _context;

    public GameUpdater(MongoContext context)
    {
        _context = context;
    }

    /// <summary>更新游戏的情感值</summary>
    public async Task<bool> UpdateEmotionAsync(int id, Emotion emotion, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Emotion, emotion), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>更新游戏的游玩状态</summary>
    public async Task<bool> UpdatePlayStateAsync(int id, PlayState playState, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.PlayState, playState), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>更新游戏的星级评分</summary>
    public async Task<bool> UpdateStarAsync(int id, int star, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Star, star), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>更新游戏的存储位置</summary>
    public async Task<bool> UpdateLocationAsync(int id, Location location, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Location, location), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>标记/取消标记游戏为重复</summary>
    public async Task<bool> MarkSameAsync(int id, bool isSame, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.IsSame, isSame), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>更新游戏的标签列表</summary>
    public async Task<bool> UpdateTagsAsync(int id, List<string> tags, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.Tag, tags), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>更新游戏的主标题和副标题</summary>
    public async Task<bool> UpdateTitleAsync(int id, string mainTitle, string subTitle, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.Eq(g => g.Id, id),
            Builders<Game>.Update.Set(g => g.MainTitle, mainTitle).Set(g => g.SubTitle, subTitle), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>设置指定角色的声优（用 ElemMatch 定位嵌套数组元素，位置操作符 $ 更新）</summary>
    public async Task<bool> SetCharCVAsync(int id, int charIndex, string cv, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.And(
                Builders<Game>.Filter.Eq(g => g.Id, id),
                Builders<Game>.Filter.ElemMatch(g => g.GameCharacters, c => c.Index == charIndex)),
            Builders<Game>.Update.Set("gamechar.$.cv", cv), cancellationToken: cancellationToken)).ModifiedCount > 0;

    /// <summary>将角色标记为男主角（SetMan）</summary>
    public async Task<bool> SetCharManAsync(int id, int charIndex, CancellationToken cancellationToken = default)
        => (await _context.Games.UpdateOneAsync(
            Builders<Game>.Filter.And(
                Builders<Game>.Filter.Eq(g => g.Id, id),
                Builders<Game>.Filter.ElemMatch(g => g.GameCharacters, c => c.Index == charIndex)),
            Builders<Game>.Update.Set("gamechar.$.man", true), cancellationToken: cancellationToken)).ModifiedCount > 0;
}
