using System.Collections.ObjectModel;

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
    /// <summary>情感值：-99 / 0 / 1 / 99</summary>
    public int Emotion { get; set; }
}

/// <summary>
/// 位置聚合项，包含存储位置值
/// </summary>
public class LocationGroupItem : GroupItem
{
    /// <summary>位置值：0=远程 / 1=本地</summary>
    public int Location { get; set; }
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
    /// <summary>游玩状态：0=未玩 / 1=进行中 / 2=已通关</summary>
    public int PlayState { get; set; }
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
