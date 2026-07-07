using System.Windows;
using System.Windows.Controls.Primitives;
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

    /// <summary>
    /// 取消编辑按钮点击，关闭编辑弹窗
    /// </summary>
    private void CancelEdit_Click(object sender, RoutedEventArgs e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            vm.IsEditModalVisible = false;
        }
    }
}
