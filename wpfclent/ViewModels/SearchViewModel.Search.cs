using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using MongoDB.Driver;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 搜索/翻页/分组切换命令
/// </summary>
public partial class SearchViewModel
{
    // ==================== 列表分组 ====================

    /// <summary>当前选中的分组类型</summary>
    [ObservableProperty]
    private GroupType _selectedGroupType = GroupType.None;

    /// <summary>是否处于分组模式</summary>
    public bool IsGrouping => SelectedGroupType != GroupType.None;

    /// <summary>是否处于非分组模式（用于 UI 可见性反转）</summary>
    public bool IsNotGrouping => SelectedGroupType == GroupType.None;

    /// <summary>分组类型变更时刷新列表，聚合面板不受影响</summary>
    partial void OnSelectedGroupTypeChanged(GroupType value)
    {
        OnPropertyChanged(nameof(IsGrouping));
        OnPropertyChanged(nameof(IsNotGrouping));
        if (!HasSearched) return;
        _ = OnGroupTypeChangedAsync(value);
    }

    private async Task OnGroupTypeChangedAsync(GroupType value)
    {
        // 切分组类型时取消并释放上一次未完成的列表查询
        var token = RenewCts(ref _listCts);

        IsLoading = true;
        try
        {
            var filter = BuildFilter();
            if (value == GroupType.None)
                await LoadPagedDataAsync(filter, token);
            else
                await LoadGroupedDataAsync(filter, token);
        }
        catch (OperationCanceledException) when (token.IsCancellationRequested)
        {
            // 主动取消，静默
        }
        finally
        {
            IsLoading = false;
        }
    }

    // ==================== 搜索命令 ====================

    /// <summary>执行搜索（重置到第一页，并行加载列表数据 + 右侧聚合）</summary>
    [RelayCommand]
    private async Task SearchAsync()
    {
        // 取消并释放上一次未完成的列表查询和聚合查询
        var listToken = RenewCts(ref _listCts);
        var aggToken = RenewCts(ref _aggCts);

        HasSearched = true;
        Page = 1;
        IsLoading = true;
        try
        {
            var filter = BuildFilter();
            // 并行：列表数据 + 右侧聚合（各自独立的 token）
            var dataTask = IsGrouping
                ? LoadGroupedDataAsync(filter, listToken)
                : LoadPagedDataAsync(filter, listToken);
            var aggTask = LoadAggregationsAsync(filter, aggToken);
            await Task.WhenAll(dataTask, aggTask);
        }
        catch (OperationCanceledException) when (listToken.IsCancellationRequested || aggToken.IsCancellationRequested)
        {
            // 主动取消是预期行为，静默吞掉
        }
        finally
        {
            IsLoading = false;
        }
    }

    /// <summary>跳转到指定页（仅分页查询，不触发聚合）</summary>
    [RelayCommand]
    private async Task GoToPageAsync(int page)
    {
        if (page < 1 || (TotalPages > 0 && page > TotalPages)) return;
        // 取消并释放上一次未完成的翻页查询
        var token = RenewCts(ref _listCts);

        Page = page;
        IsLoading = true;
        try
        {
            await LoadPagedDataAsync(BuildFilter(), token);
        }
        catch (OperationCanceledException) when (token.IsCancellationRequested)
        {
            // 主动取消，静默
        }
        finally
        {
            IsLoading = false;
        }
    }
}
