using CommunityToolkit.Mvvm.ComponentModel;
using MongoDB.Bson.Serialization.Attributes;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 游戏主模型，对应 MongoDB game 集合的文档结构
/// 使用 CommunityToolkit.Mvvm 源码生成器实现属性变更通知
/// </summary>
[BsonIgnoreExtraElements]
public partial class Game : ObservableObject
{
    /// <summary>游戏唯一 ID（对应 MongoDB _id）</summary>
    [ObservableProperty]
    [property: BsonElement("_id")]
    private int _id;

    /// <summary>完整名称（原始数据）</summary>
    [ObservableProperty]
    [property: BsonElement("name")]
    private string _name = string.Empty;

    /// <summary>主标题</summary>
    [ObservableProperty]
    [property: BsonElement("mainTitle")]
    private string _mainTitle = string.Empty;

    /// <summary>副标题</summary>
    [ObservableProperty]
    [property: BsonElement("subTitle")]
    private string _subTitle = string.Empty;

    /// <summary>发布日期</summary>
    [ObservableProperty]
    [property: BsonElement("publishDate")]
    private DateTime _publishDate;

    /// <summary>星级评分 0~5</summary>
    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(ShowStar))]
    [NotifyPropertyChangedFor(nameof(ShowEmotion))]
    [property: BsonElement("star")]
    private int _star;

    /// <summary>情感值：-99(嫌い) / 0(...) / 1(気になり) / 99(好き)</summary>
    [ObservableProperty]
    [property: BsonElement("emotion")]
    private int _emotion;

    /// <summary>存储位置：0=云端 / 1=本地</summary>
    [ObservableProperty]
    [property: BsonElement("location")]
    private int _location;

    /// <summary>游玩状态：0=未玩 / 1=进行中 / 2=已通关</summary>
    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(ShowStar))]
    [NotifyPropertyChangedFor(nameof(ShowEmotion))]
    [property: BsonElement("playState")]
    private int _playState;

    /// <summary>是否标记为重复游戏</summary>
    [ObservableProperty]
    [property: BsonElement("isSame")]
    private bool _isSame;

    /// <summary>标签列表</summary>
    [ObservableProperty]
    [property: BsonElement("tag")]
    private List<string> _tag = new();

    /// <summary>画师列表</summary>
    [ObservableProperty]
    [property: BsonElement("painter")]
    private List<string> _painter = new();

    /// <summary>剧本作家列表</summary>
    [ObservableProperty]
    [property: BsonElement("writer")]
    private List<string> _writer = new();

    /// <summary>品牌信息</summary>
    [ObservableProperty]
    [property: BsonElement("brand")]
    private Brand? _brand;

    /// <summary>故事简介</summary>
    [ObservableProperty]
    [property: BsonElement("story")]
    private string _story = string.Empty;

    /// <summary>游戏介绍</summary>
    [ObservableProperty]
    [property: BsonElement("intro")]
    private string _intro = string.Empty;

    /// <summary>角色列表</summary>
    [ObservableProperty]
    [property: BsonElement("gamechar")]
    private List<GameCharacter> _gameCharacters = new();

    /// <summary>截图列表</summary>
    [ObservableProperty]
    [property: BsonElement("simpleImg")]
    private List<GameImg> _simpleImgs = new();

    /// <summary>
    /// 图片基础路径，基于 ImageConfig 配置 + PublishDate + Id 计算
    /// </summary>
    [BsonIgnore]
    public string ImgBase => ImageConfig.BuildImgBase(PublishDate.Year, PublishDate.Month, Id);

    /// <summary>封面图完整 URL</summary>
    [BsonIgnore]
    public string CoverUrl => $"{ImgBase}game_s.jpg";

    /// <summary>故事图完整 URL</summary>
    [BsonIgnore]
    public string StoryImageUrl => $"{ImgBase}game_l.jpg";

    /// <summary>是否已发布（当前时间 >= 发布日期）</summary>
    [BsonIgnore]
    public bool Published => DateTime.Now >= PublishDate;

    /// <summary>是否显示星级控件：已发布且（已通关或有评分）</summary>
    [BsonIgnore]
    public bool ShowStar => Published && (PlayState == 2 || Star > 0);

    /// <summary>是否显示情感按钮：未通关时显示</summary>
    [BsonIgnore]
    public bool ShowEmotion => PlayState != 2 && PlayState != 1;
}

/// <summary>
/// 声优信息模型（对应 MongoDB cv 集合）
/// </summary>
[BsonIgnoreExtraElements]
public class CV
{
    /// <summary>声优名</summary>
    [BsonElement("name")]
    public string Name { get; set; } = string.Empty;

    /// <summary>声优星级评分</summary>
    [BsonElement("star")]
    public int Star { get; set; }
}

/// <summary>
/// 游戏角色模型
/// </summary>
[BsonIgnoreExtraElements]
public partial class GameCharacter : ObservableObject
{
    /// <summary>角色名</summary>
    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(CharTitle))]
    [NotifyPropertyChangedFor(nameof(CharSubTitle))]
    [property: BsonElement("name")]
    private string _name = string.Empty;

    /// <summary>声优（马甲名）</summary>
    [ObservableProperty]
    [property: BsonElement("cv")]
    private string _cv = string.Empty;

    /// <summary>角色介绍</summary>
    [ObservableProperty]
    [property: BsonElement("intro")]
    private string _intro = string.Empty;

    /// <summary>去马甲后的真实声优名</summary>
    [ObservableProperty]
    [property: BsonElement("truecv")]
    private string _trueCV = string.Empty;

    /// <summary>角色在图库中的索引序号</summary>
    [ObservableProperty]
    [property: BsonElement("index")]
    private int _index;

    /// <summary>是否男主角（数据库字段）</summary>
    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(Heroine))]
    [property: BsonElement("man")]
    private bool _man;

    /// <summary>是否女主角（由 man 推导）</summary>
    [BsonIgnore]
    public bool Heroine => !Man;

    /// <summary>角色头像 URL，由 GameEnricher 计算</summary>
    [BsonIgnore]
    public string AvatarUrl { get; set; } = string.Empty;

    /// <summary>声优评分数据，由 GameEnricher 查询 cv 集合后匹配 trueCV 填充</summary>
    [BsonIgnore]
    public CV? CvObj { get; set; }

    /// <summary>角色名标题部分（第二个空格后内容，如"橘花"），基于 Name 实时计算</summary>
    [BsonIgnore]
    public string CharTitle
    {
        get
        {
            if (string.IsNullOrEmpty(Name)) return string.Empty;
            var parts = Name.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
            return parts.Length > 1 ? string.Join(" ", parts.Skip(1)) : Name;
        }
    }

    /// <summary>角色名副标题部分（第一个空格前内容，如"春川 ミミ"中为"春川"），基于 Name 实时计算</summary>
    [BsonIgnore]
    public string CharSubTitle
    {
        get
        {
            if (string.IsNullOrEmpty(Name)) return string.Empty;
            var parts = Name.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
            return parts.Length > 1 ? parts[0] : string.Empty;
        }
    }
}

/// <summary>
/// 游戏截图模型
/// </summary>
[BsonIgnoreExtraElements]
public class GameImg
{
    /// <summary>截图索引序号</summary>
    [BsonElement("index")]
    public int Index { get; set; }

    /// <summary>截图 URL，由 GameEnricher 计算</summary>
    [BsonIgnore]
    public string Url { get; set; } = string.Empty;
}

/// <summary>
/// 品牌信息模型
/// </summary>
[BsonIgnoreExtraElements]
public class Brand
{
    /// <summary>品牌 ID</summary>
    [BsonElement("id")]
    public int Id { get; set; }

    /// <summary>品牌名称</summary>
    [BsonElement("name")]
    public string Name { get; set; } = string.Empty;

    /// <summary>品牌官网</summary>
    [BsonElement("website")]
    public string Website { get; set; } = string.Empty;

    /// <summary>所属公司</summary>
    [BsonElement("comp")]
    public string Comp { get; set; } = string.Empty;
}
