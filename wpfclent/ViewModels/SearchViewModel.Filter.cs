using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 搜索条件状态与命令（日期范围、包含/排除条件）
/// </summary>
public partial class SearchViewModel
{
    // ==================== 搜索条件 ====================

    /// <summary>当前选中的日期预设 key</summary>
    [ObservableProperty]
    private string _selectedDatePreset = "THIS_MONTH";

    /// <summary>是否启用日期范围筛选</summary>
    [ObservableProperty]
    private bool _isDateRangeEnabled = true;

    /// <summary>当前日期范围</summary>
    [ObservableProperty]
    private DateRange _currentDateRange = DateRangeHelper.GetDateRange("THIS_MONTH");

    /// <summary>包含条件列表（满足才显示）</summary>
    [ObservableProperty]
    private ObservableCollection<SearchOpt> _includeOpts = new();

    /// <summary>排除条件列表（满足则隐藏）</summary>
    [ObservableProperty]
    private ObservableCollection<SearchOpt> _excludeOpts = new();

    /// <summary>是否选择了"自定义"日期预设</summary>
    public bool IsCustomDatePreset => SelectedDatePreset == "CUSTOM";

    /// <summary>日期范围显示文本</summary>
    public string DateRangeDisplay => $"{CurrentDateRange.StartMonth} ~ {CurrentDateRange.EndMonth}";

    /// <summary>日期预设变更时同步日期范围，自定义模式不清除已有范围</summary>
    partial void OnSelectedDatePresetChanged(string value)
    {
        OnPropertyChanged(nameof(IsCustomDatePreset));
        if (value != "CUSTOM")
        {
            CurrentDateRange = DateRangeHelper.GetDateRange(value);
        }
    }

    partial void OnCurrentDateRangeChanged(DateRange value)
    {
        OnPropertyChanged(nameof(DateRangeDisplay));
    }

    /// <summary>添加一条搜索条件</summary>
    [RelayCommand]
    private void AddSearchOpt(string filterType)
    {
        var f = Enum.Parse<FilterType>(filterType);
        var opt = new SearchOpt(SearchType.TAG, "", true, f);
        if (f == FilterType.Include)
            IncludeOpts.Add(opt);
        else
            ExcludeOpts.Add(opt);
    }

    /// <summary>删除一条搜索条件</summary>
    [RelayCommand]
    private void RemoveSearchOpt(SearchOpt opt)
    {
        if (opt.Filter == FilterType.Include)
            IncludeOpts.Remove(opt);
        else
            ExcludeOpts.Remove(opt);
    }

    /// <summary>交换搜索条件的包含/排除类型</summary>
    [RelayCommand]
    private void SwapSearchOpt(SearchOpt opt)
    {
        if (opt.Filter == FilterType.Include)
        {
            IncludeOpts.Remove(opt);
            opt.Filter = FilterType.Exclude;
            ExcludeOpts.Add(opt);
        }
        else
        {
            ExcludeOpts.Remove(opt);
            opt.Filter = FilterType.Include;
            IncludeOpts.Add(opt);
        }
    }

    /// <summary>快捷调整日期范围（+/- 月或年，或跳转到本月）</summary>
    [RelayCommand]
    private void AdjustDateRange(string direction)
    {
        if (direction == "this_month")
        {
            SelectedDatePreset = "THIS_MONTH";
            CurrentDateRange = DateRangeHelper.GetDateRange("THIS_MONTH");
            return;
        }

        var (delta, isYear) = direction switch
        {
            "+1y" => (1, true),
            "-1y" => (-1, true),
            "+1m" => (1, false),
            "-1m" => (-1, false),
            _ => (0, false),
        };

        if (delta != 0)
        {
            SelectedDatePreset = "CUSTOM";
            CurrentDateRange = DateRangeHelper.AdjustDateRange(CurrentDateRange, delta, isYear);
        }
    }
}
