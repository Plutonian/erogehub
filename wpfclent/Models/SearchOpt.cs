using CommunityToolkit.Mvvm.ComponentModel;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 搜索条件
/// </summary>
public partial class SearchOpt : ObservableObject
{
    [ObservableProperty]
    private SearchType _key = SearchType.TAG;

    [ObservableProperty]
    private string _value = string.Empty;

    [ObservableProperty]
    private bool _enabled = true;

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
