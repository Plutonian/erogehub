using CommunityToolkit.Mvvm.ComponentModel;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 搜索条件模型，对应左侧搜索面板中的一条包含/排除条件
/// </summary>
public partial class SearchOpt : ObservableObject
{
    /// <summary>搜索类型（TAG / STAR / EMOTION 等）</summary>
    [ObservableProperty]
    private SearchType _key = SearchType.TAG;

    /// <summary>搜索值（文本、星级数字、枚举值字符串等）</summary>
    [ObservableProperty]
    private string _value = string.Empty;

    /// <summary>是否启用该条件</summary>
    [ObservableProperty]
    private bool _enabled = true;

    /// <summary>筛选类型：包含 或 排除</summary>
    [ObservableProperty]
    private FilterType _filter = FilterType.Include;

    public SearchOpt() { }

    public SearchOpt(SearchType key, string value, bool enabled, FilterType filter)
    {
        Key = key;
        Value = value;
        Enabled = enabled;
        Filter = filter;
    }
}
