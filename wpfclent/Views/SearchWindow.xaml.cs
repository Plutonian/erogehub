using System.Windows;
using ErogeHub.Wpf.Models;
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
    /// 分页控件的翻页事件（HandyControl Pagination 控件不支持 Command 绑定）
    /// </summary>
    private void Pagination_PageUpdated(object sender, FunctionEventArgs<int> e)
    {
        if (_viewModel.GoToPageCommand.CanExecute(e.Info))
        {
            _viewModel.GoToPageCommand.Execute(e.Info);
        }
    }

    /// <summary>品牌树选中：添加 BRAND 包含条件（TreeView.SelectedItem 不支持双向绑定，保留 code-behind 转发）</summary>
    private void BrandTree_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
    {
        if (e.NewValue is BrandGroupItem item)
        {
            _viewModel.BrandClickedCommand.Execute(item);
        }
    }
}

