using CommunityToolkit.Mvvm.ComponentModel;
using MongoDB.Bson.Serialization.Attributes;

namespace ErogeHub.Wpf.Models;

public partial class Game : ObservableObject
{
    [ObservableProperty]
    [property: BsonElement("_id")]
    private int _id;

    [ObservableProperty]
    [property: BsonElement("name")]
    private string _name = string.Empty;

    [ObservableProperty]
    [property: BsonElement("mainTitle")]
    private string _mainTitle = string.Empty;

    [ObservableProperty]
    [property: BsonElement("subTitle")]
    private string _subTitle = string.Empty;

    [ObservableProperty]
    [property: BsonElement("publishDate")]
    private DateTime _publishDate;

    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(ShowStar))]
    [NotifyPropertyChangedFor(nameof(ShowEmotion))]
    [property: BsonElement("star")]
    private int _star;

    [ObservableProperty]
    [property: BsonElement("emotion")]
    private int _emotion;

    [ObservableProperty]
    [property: BsonElement("location")]
    private int _location;

    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(ShowStar))]
    [NotifyPropertyChangedFor(nameof(ShowEmotion))]
    [property: BsonElement("playState")]
    private int _playState;

    [ObservableProperty]
    [property: BsonElement("isSame")]
    private bool _isSame;

    [ObservableProperty]
    [property: BsonElement("tag")]
    private List<string> _tag = new();

    [ObservableProperty]
    [property: BsonElement("painter")]
    private List<string> _painter = new();

    [ObservableProperty]
    [property: BsonElement("writer")]
    private List<string> _writer = new();

    [ObservableProperty]
    [property: BsonElement("brand")]
    private Brand? _brand;

    [ObservableProperty]
    [property: BsonElement("story")]
    private string _story = string.Empty;

    [ObservableProperty]
    [property: BsonElement("intro")]
    private string _intro = string.Empty;

    [ObservableProperty]
    [property: BsonElement("gamechar")]
    private List<GameCharacter> _gameCharacters = new();

    [ObservableProperty]
    [property: BsonElement("simpleImg")]
    private List<GameImg> _simpleImgs = new();

    /// <summary>
    /// 图片基础路径，由 MongoService 设置：http://{server}/img/{year}/{month}/{id}/
    /// </summary>
    [BsonIgnore]
    public string ImgBase { get; set; } = string.Empty;

    [BsonIgnore]
    public string CoverUrl { get; set; } = string.Empty;

    [BsonIgnore]
    public string StoryImageUrl { get; set; } = string.Empty;

    [BsonIgnore]
    public bool Published => DateTime.Now >= PublishDate;

    [BsonIgnore]
    public bool ShowStar => Published && (PlayState == 2 || Star > 0);

    [BsonIgnore]
    public bool ShowEmotion => PlayState != 2 && PlayState != 1;
}

public partial class GameCharacter : ObservableObject
{
    [ObservableProperty]
    [property: BsonElement("name")]
    private string _name = string.Empty;

    [ObservableProperty]
    [property: BsonElement("cv")]
    private string _cv = string.Empty;

    [ObservableProperty]
    [property: BsonElement("intro")]
    private string _intro = string.Empty;

    [ObservableProperty]
    [property: BsonElement("truecv")]
    private string _trueCV = string.Empty;

    [ObservableProperty]
    [property: BsonElement("index")]
    private int _index;

    [ObservableProperty]
    [property: BsonElement("heroine")]
    private bool _heroine;

    [BsonIgnore]
    public string AvatarUrl { get; set; } = string.Empty;
}

public class GameImg
{
    [BsonElement("index")]
    public int Index { get; set; }

    [BsonIgnore]
    public string Url { get; set; } = string.Empty;
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
