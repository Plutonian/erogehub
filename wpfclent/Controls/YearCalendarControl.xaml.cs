using System.Collections;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.Controls;

/// <summary>
/// 单年日历控件 - 参照 WEB month-calendar 实现
/// 将某一年（DateGroupItem）的月份子节点补齐为 4×3 网格展示：
/// 有游戏的月份蓝底白字，无游戏的月份灰底灰字，纯展示无点击交互。
/// </summary>
public partial class YearCalendarControl : UserControl
{
    public YearCalendarControl()
    {
        InitializeComponent();
    }

    /// <summary>年份分组项（Children 为该年的月节点集合）</summary>
    public static readonly DependencyProperty YearProperty =
        DependencyProperty.Register(nameof(Year), typeof(DateGroupItem), typeof(YearCalendarControl),
            new PropertyMetadata(null, OnYearChanged));

    public DateGroupItem? Year
    {
        get => (DateGroupItem?)GetValue(YearProperty);
        set => SetValue(YearProperty, value);
    }

    /// <summary>
    /// 日期点击命令 - 保留接口供将来使用，当前未接线（无点击交互）。
    /// </summary>
    public static readonly DependencyProperty DateClickedCommandProperty =
        DependencyProperty.Register(nameof(DateClickedCommand), typeof(ICommand), typeof(YearCalendarControl));

    public ICommand? DateClickedCommand
    {
        get => (ICommand?)GetValue(DateClickedCommandProperty);
        set => SetValue(DateClickedCommandProperty, value);
    }

    /// <summary>补齐后的 12 个月列表（1~12月），供网格绑定</summary>
    public static readonly DependencyProperty MonthsProperty =
        DependencyProperty.Register(nameof(Months), typeof(IList), typeof(YearCalendarControl),
            new PropertyMetadata(null));

    public IList? Months
    {
        get => (IList?)GetValue(MonthsProperty);
        set => SetValue(MonthsProperty, value);
    }

    private static void OnYearChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
    {
        if (d is YearCalendarControl ctrl)
            ctrl.RebuildMonths();
    }

    /// <summary>
    /// 将年份的月份子节点补齐为固定 12 格列表：
    /// 缺失月份用 Count=0 的占位项填充。
    /// </summary>
    private void RebuildMonths()
    {
        var byIndex = new Dictionary<int, DateGroupItem>();
        if (Year?.Children != null)
        {
            foreach (var child in Year.Children)
            {
                if (child.Index is >= 1 and <= 12)
                    byIndex[child.Index] = child;
            }
        }

        var months = new List<DateGroupItem>();
        for (var m = 1; m <= 12; m++)
        {
            months.Add(byIndex.TryGetValue(m, out var real)
                ? real
                : new DateGroupItem { Title = $"{m}月", Count = 0, Index = m });
        }
        Months = months;
    }
}
