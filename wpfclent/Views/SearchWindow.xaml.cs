using System.Windows;
using System.Windows.Controls;
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
    /// 分页控件的翻页事件
    /// </summary>
    private void Pagination_PageUpdated(object sender, FunctionEventArgs<int> e)
    {
        if (_viewModel.GoToPageCommand.CanExecute(e.Info))
        {
            _viewModel.GoToPageCommand.Execute(e.Info);
        }
    }

    /// <summary>星级列表选中：添加 STAR 包含条件</summary>
    private void StarListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        if (e.AddedItems.Count > 0 && e.AddedItems[0] is StarGroupItem item)
        {
            _viewModel.StarClickedCommand.Execute(item);
        }
    }

    /// <summary>情感列表选中：添加 EMOTION 包含条件</summary>
    private void EmotionListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        if (e.AddedItems.Count > 0 && e.AddedItems[0] is EmotionGroupItem item)
        {
            _viewModel.EmotionClickedCommand.Execute(item);
        }
    }

    /// <summary>位置列表选中：添加 LOCATION 包含条件</summary>
    private void LocationListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        if (e.AddedItems.Count > 0 && e.AddedItems[0] is LocationGroupItem item)
        {
            _viewModel.LocationClickedCommand.Execute(item);
        }
    }

    /// <summary>游玩状态列表选中：添加 PLAYSTATE 包含条件</summary>
    private void PlayStateListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        if (e.AddedItems.Count > 0 && e.AddedItems[0] is PlayStateGroupItem item)
        {
            _viewModel.PlayStateClickedCommand.Execute(item);
        }
    }

    /// <summary>品牌树选中：添加 BRAND 包含条件</summary>
    private void BrandTree_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
    {
        if (e.NewValue is BrandGroupItem item)
        {
            _viewModel.BrandClickedCommand.Execute(item);
        }
    }
}

