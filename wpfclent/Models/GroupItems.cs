using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Globalization;
using System.Linq;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 聚合结果基础项：标题 + 计数，用于右侧面板的通用聚合数据
/// </summary>
public class GroupItem
{
    /// <summary>聚合项显示名称</summary>
    public string Title { get; set; } = string.Empty;

    /// <summary>命中数量</summary>
    public int Count { get; set; }
}

/// <summary>
/// 情感聚合项，包含情感值
/// </summary>
public class EmotionGroupItem : GroupItem
{
    /// <summary>情感值</summary>
    public Emotion Emotion { get; set; }
}

/// <summary>
/// 位置聚合项，包含存储位置值
/// </summary>
public class LocationGroupItem : GroupItem
{
    /// <summary>存储位置</summary>
    public Location Location { get; set; }
}

/// <summary>
/// 星级聚合项，包含星级值
/// </summary>
public class StarGroupItem : GroupItem
{
    /// <summary>星级 0~5</summary>
    public int Star { get; set; }
}

/// <summary>
/// 游玩状态聚合项，包含状态值
/// </summary>
public class PlayStateGroupItem : GroupItem
{
    /// <summary>游玩状态</summary>
    public PlayState PlayState { get; set; }
}

/// <summary>
/// 日期分组项（支持年/月层级），用于右侧面板的 TreeView
/// </summary>
public class DateGroupItem : GroupItem
{
    /// <summary>分组索引（年份或月份数字）</summary>
    public int Index { get; set; }

    /// <summary>日期范围起始</summary>
    public DateTime Start { get; set; }

    /// <summary>日期范围结束</summary>
    public DateTime End { get; set; }

    /// <summary>分组粒度：年 / 月</summary>
    public DateType DateType { get; set; }

    /// <summary>子节点（月节点）</summary>
    public ObservableCollection<DateGroupItem> Children { get; set; } = new();
}

/// <summary>
/// 列表分组项：标题 + 该组包含的游戏列表
/// 用于中间面板分组显示模式
/// </summary>
public class GameGroup
{
    /// <summary>分组标题（如"2024年"、"3★"、"好き"）</summary>
    public string Title { get; set; } = string.Empty;

    /// <summary>该分组下的游戏列表</summary>
    public ObservableCollection<Game> Games { get; set; } = new();
}

/// <summary>
/// 分组键：含显示名 + 排序权重，用于 CollectionView 分组时控制组顺序
/// </summary>
public class GameGroupKey : IComparable, IComparable<GameGroupKey>, IEquatable<GameGroupKey>
{
    /// <summary>分组显示名称（如"2024年"、"3★"、"好き"）</summary>
    public string Display { get; init; } = string.Empty;

    /// <summary>排序权重（升序，值越小排越前）</summary>
    public double Sort { get; init; }

    /// <summary>分组唯一标识（用于去重和匹配排序权重）</summary>
    public string Identity { get; init; } = string.Empty;

    public override string ToString() => Display;

    public int CompareTo(GameGroupKey? other) => other is null ? 1 : Sort.CompareTo(other.Sort);

    public int CompareTo(object? obj) => obj switch
    {
        null => 1,
        GameGroupKey k => CompareTo(k),
        _ => 1
    };

    public override bool Equals(object? obj) => obj is GameGroupKey k && Identity == k.Identity;

    public override int GetHashCode() => Identity.GetHashCode();

    public bool Equals(GameGroupKey? other) => other is not null && Identity == other.Identity;
}

/// <summary>
/// 按 GroupType 对 Game 进行分组的 GroupDescription
/// 配合 VirtualizingWrapPanel 的分组虚拟化使用
/// </summary>
public class GameGroupDescription : GroupDescription
{
    private readonly GroupType _groupType;
    private readonly Dictionary<string, double> _sortWeights;

    /// <param name="groupType">分组类型</param>
    /// <param name="sortWeights">分组标识 → 排序权重（升序，值越小排越前）</param>
    public GameGroupDescription(GroupType groupType, Dictionary<string, double> sortWeights)
    {
        _groupType = groupType;
        _sortWeights = sortWeights;
    }

    public override object? GroupNameFromItem(object item, int level, CultureInfo culture)
    {
        if (item is not Game game) return null;

        var (identity, display) = GetGroupIdentity(game, _groupType);
        _sortWeights.TryGetValue(identity, out var sort);
        return new GameGroupKey { Display = display, Sort = sort, Identity = identity };
    }

    /// <summary>从 Game 算出分组标识和显示名</summary>
    private static (string identity, string display) GetGroupIdentity(Game game, GroupType type)
    {
        return type switch
        {
            GroupType.Year => game.PublishDate.Year > 1
                ? (game.PublishDate.Year.ToString(), $"{game.PublishDate.Year}年")
                : ("0", "无日期"),

            GroupType.Month => game.PublishDate.Year > 1
                ? (game.PublishDate.ToString("yyyy-MM"), game.PublishDate.ToString("yyyy-MM"))
                : ("0000-00", "无日期"),

            GroupType.Star => (game.Star.ToString(), $"{game.Star}★"),

            GroupType.Emotion => (game.Emotion.ToString(), game.Emotion.Label()),

            GroupType.Location => (game.Location.ToString(), game.Location.ToString()),

            GroupType.PlayState => (game.PlayState.ToString(), game.PlayState.Label()),

            GroupType.Brand => (
                game.Brand != null && !string.IsNullOrEmpty(game.Brand.Comp) ? game.Brand.Comp
                : game.Brand != null && !string.IsNullOrEmpty(game.Brand.Name) ? game.Brand.Name
                : "无品牌",
                game.Brand != null && !string.IsNullOrEmpty(game.Brand.Comp) ? game.Brand.Comp
                : game.Brand != null && !string.IsNullOrEmpty(game.Brand.Name) ? game.Brand.Name
                : "无品牌"
            ),

            _ => ("unknown", "unknown")
        };
    }

    /// <summary>预计算各分组的排序权重（升序，值越小排越前），供 GroupNameFromItem 控制组顺序</summary>
    public static Dictionary<string, double> ComputeSortWeights(List<Game> games, GroupType type)
    {
        return type switch
        {
            GroupType.Year => games
                .Select(g => g.PublishDate.Year > 1 ? g.PublishDate.Year : 0)
                .Distinct()
                .OrderByDescending(y => y)
                .Select((y, i) => (key: y.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Month => games
                .Select(g => g.PublishDate.Year > 1 ? g.PublishDate.ToString("yyyy-MM") : "0000-00")
                .Distinct()
                .OrderByDescending(s => s)
                .Select((s, i) => (key: s, rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Star => games
                .Select(g => g.Star)
                .Distinct()
                .OrderByDescending(s => s)
                .Select((s, i) => (key: s.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Emotion => games
                .Select(g => g.Emotion)
                .Distinct()
                .OrderByDescending(e => e)
                .Select((e, i) => (key: e.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Location => games
                .Select(g => g.Location)
                .Distinct()
                .OrderByDescending(l => l)
                .Select((l, i) => (key: l.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.PlayState => games
                .Select(g => g.PlayState)
                .Distinct()
                .OrderByDescending(p => p)
                .Select((p, i) => (key: p.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Brand => games
                .Select(g => g.Brand != null && !string.IsNullOrEmpty(g.Brand.Comp) ? g.Brand.Comp
                            : g.Brand != null && !string.IsNullOrEmpty(g.Brand.Name) ? g.Brand.Name
                            : "无品牌")
                .GroupBy(name => name)
                .OrderByDescending(g => g.Count())
                .Select((g, i) => (key: g.Key, rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            _ => new Dictionary<string, double>()
        };
    }
}

/// <summary>
/// 品牌分组项（支持公司→品牌层级），用于右侧面板的 TreeView
/// </summary>
public class BrandGroupItem : GroupItem
{
    /// <summary>所属公司名称（可选）</summary>
    public string? Comp { get; set; }

    /// <summary>品牌信息</summary>
    public Brand? Brand { get; set; }

    /// <summary>子节点（品牌节点）</summary>
    public ObservableCollection<BrandGroupItem> Children { get; set; } = new();
}
