using System.Windows;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Navigation;
using ErogeHub.Wpf.ViewModels;
using HandyControl.Data;

namespace ErogeHub.Wpf.Views;

/// <summary>
/// 游戏详情窗口 - 用于查看和编辑游戏详细信息
/// </summary>
public partial class GameDetailWindow : Window
{
    public GameDetailWindow()
    {
        InitializeComponent();
    }

    /// <summary>
    /// 星级评分变更事件，同步写入数据库
    /// </summary>
    private void DetailRate_ValueChanged(object sender, FunctionEventArgs<double> e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            _ = vm.UpdateStarAsync((int)e.Info);
        }
    }

    /// <summary>
    /// "标记相同" ToggleButton 点击事件，同步写入数据库
    /// </summary>
    private void DetailMarkSame_Click(object sender, RoutedEventArgs e)
    {
        if (sender is ToggleButton tb && DataContext is GameDetailViewModel vm)
        {
            _ = vm.ToggleMarkSameAsync(tb.IsChecked == true);
        }
    }

    /// <summary>
    /// 超链接导航事件，用默认浏览器打开 URL
    /// </summary>
    private void OpenHyperlink_RequestNavigate(object sender, RequestNavigateEventArgs e)
    {
        try
        {
            System.Diagnostics.Process.Start(new System.Diagnostics.ProcessStartInfo(e.Uri.ToString()) { UseShellExecute = true });
        }
        catch { }
        e.Handled = true;
    }

    // ==================== Tag 添加（仿 web 版） ====================

    /// <summary>点击 "+ 新标签" pill，显示输入框</summary>
    private void TagAddPill_Click(object sender, MouseButtonEventArgs e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            vm.IsAddingTag = true;
        }
    }

    /// <summary>输入框按 Enter 确认添加</summary>
    private void TagInput_KeyDown(object sender, System.Windows.Input.KeyEventArgs e)
    {
        if (e.Key == Key.Enter && DataContext is GameDetailViewModel vm)
        {
            vm.AddTagCommand.Execute(null);
            vm.IsAddingTag = false;
        }
    }

    /// <summary>输入框失去焦点时确认添加</summary>
    private void TagInput_LostFocus(object sender, RoutedEventArgs e)
    {
        if (DataContext is GameDetailViewModel vm && vm.IsAddingTag)
        {
            vm.AddTagCommand.Execute(null);
            vm.IsAddingTag = false;
        }
    }
}
