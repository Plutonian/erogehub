using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Navigation;
using ErogeHub.Wpf.ViewModels;
using HandyControl.Data;

namespace ErogeHub.Wpf.Views;

/// <summary>
/// 搜索主窗口 - 三栏布局：左侧搜索条件 | 中间游戏卡片列表 | 右侧聚合分析面板
/// </summary>
public partial class SearchWindow : Window
{
    /// <summary>搜索视图模型</summary>
    private readonly SearchViewModel _viewModel;

    public SearchWindow(SearchViewModel viewModel)
    {
        InitializeComponent();
        _viewModel = viewModel;
        DataContext = _viewModel;
    }

    /// <summary>
    /// 分页控件的翻页事件
    /// </summary>
    private void Pagination_PageUpdated(object sender, FunctionEventArgs<int> e)
    {
        if (_viewModel.GoToPageCommand.CanExecute(e.Info))
        {
            _viewModel.GoToPageCommand.Execute(e.Info);
        }
    }

    /// <summary>
    /// 游戏卡片上的星级评分变更事件，同步写入数据库
    /// </summary>
    private void CardRate_ValueChanged(object sender, FunctionEventArgs<double> e)
    {
        if (sender is FrameworkElement fe && fe.DataContext is Models.Game game)
        {
            _ = _viewModel.UpdateCardStarAsync(game.Id, (int)e.Info);
        }
    }

    /// <summary>
    /// 游戏卡片上"标记相同" ToggleButton 点击事件，同步写入数据库
    /// </summary>
    private void CardMarkSame_Click(object sender, RoutedEventArgs e)
    {
        if (sender is ToggleButton tb && tb.DataContext is Models.Game game)
        {
            _ = _viewModel.UpdateCardMarkSameAsync(game.Id, tb.IsChecked == true);
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
}

