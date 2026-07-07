using System.Collections.ObjectModel;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 聚合结果基础项
/// </summary>
public class GroupItem
{
    public string Title { get; set; } = string.Empty;
    public int Count { get; set; }
}

public class EmotionGroupItem : GroupItem
{
    public int Emotion { get; set; }
}

public class LocationGroupItem : GroupItem
{
    public int Location { get; set; }
}

public class StarGroupItem : GroupItem
{
    public int Star { get; set; }
}

public class PlayStateGroupItem : GroupItem
{
    public int PlayState { get; set; }
}

/// <summary>
/// 日期分组项（支持年/月层级）
/// </summary>
public class DateGroupItem : GroupItem
{
    public int Index { get; set; }
    public DateTime Start { get; set; }
    public DateTime End { get; set; }
    public DateType DateType { get; set; }
    public ObservableCollection<DateGroupItem> Children { get; set; } = new();
}

/// <summary>
/// 品牌分组项（支持公司→品牌层级）
/// </summary>
public class BrandGroupItem : GroupItem
{
    public string? Comp { get; set; }
    public Brand? Brand { get; set; }
    public ObservableCollection<BrandGroupItem> Children { get; set; } = new();
}
