using MongoDB.Bson.Serialization.Attributes;

namespace ErogeHub.Wpf.Models;

public class Game
{
    [BsonElement("_id")]
    public int Id { get; set; }

    [BsonElement("name")]
    public string Name { get; set; } = string.Empty;

    [BsonElement("mainTitle")]
    public string MainTitle { get; set; } = string.Empty;

    [BsonElement("subTitle")]
    public string SubTitle { get; set; } = string.Empty;

    [BsonElement("publishDate")]
    public DateTime PublishDate { get; set; }

    [BsonElement("star")]
    public int Star { get; set; }

    [BsonElement("emotion")]
    public int Emotion { get; set; }

    [BsonElement("location")]
    public int Location { get; set; }

    [BsonElement("playState")]
    public int PlayState { get; set; }

    [BsonElement("isSame")]
    public bool IsSame { get; set; }

    [BsonElement("tag")]
    public List<string> Tag { get; set; } = new();

    [BsonElement("painter")]
    public List<string> Painter { get; set; } = new();

    [BsonElement("writer")]
    public List<string> Writer { get; set; } = new();

    [BsonElement("brand")]
    public Brand? Brand { get; set; }

    /// <summary>
    /// 封面图 URL，由 MongoService 根据发布日期和 ID 计算
    /// </summary>
    [BsonIgnore]
    public string CoverUrl { get; set; } = string.Empty;
}

public class Brand
{
    [BsonElement("id")]
    public int Id { get; set; }

    [BsonElement("name")]
    public string Name { get; set; } = string.Empty;

    [BsonElement("website")]
    public string Website { get; set; } = string.Empty;

    [BsonElement("comp")]
    public string Comp { get; set; } = string.Empty;
}
