using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// MongoDB 共享上下文：持有 game / cv 集合引用，提供过滤条件工具方法。
/// 供 Repository / Aggregator / Updater 共用，避免重复持有集合引用。
/// </summary>
public class MongoContext
{
    /// <summary>MongoDB game 集合（强类型，driver 自动映射）</summary>
    public IMongoCollection<Game> Games { get; }

    /// <summary>MongoDB cv 集合（强类型）</summary>
    public IMongoCollection<CV> CVs { get; }

    public MongoContext(string connectionString, string databaseName)
    {
        var client = new MongoClient(connectionString);
        var database = client.GetDatabase(databaseName);
        Games = database.GetCollection<Game>("game");
        CVs = database.GetCollection<CV>("cv");
    }

    /// <summary>空安全：null filter 返回 Empty，否则原样使用（FilterDefinition 已是强类型）</summary>
    public static FilterDefinition<Game> ToGameFilter(FilterDefinition<Game>? filter)
        => filter ?? Builders<Game>.Filter.Empty;

    /// <summary>把强类型 FilterDefinition 渲染成 BsonDocument，供聚合管道的 $match 阶段使用</summary>
    public BsonDocument ToMatchDoc(FilterDefinition<Game>? filter)
    {
        if (filter == null) return new BsonDocument();
        var args = new RenderArgs<Game>(Games.DocumentSerializer, Games.Settings.SerializerRegistry);
        return filter.Render(args).AsBsonDocument;
    }

    /// <summary>安全的 int 取值，null 返回 0</summary>
    public static int SafeInt(BsonValue v) => v is null or BsonNull ? 0 : v.AsInt32;

    /// <summary>安全的 string 取值，null 返回空串</summary>
    public static string SafeStr(BsonValue v) => v is null or BsonNull ? "" : v.AsString;

    /// <summary>安全的 bool 取值，非布尔类型返回 false</summary>
    public static bool SafeBool(BsonValue v) => v is BsonBoolean b && b.Value;
}
