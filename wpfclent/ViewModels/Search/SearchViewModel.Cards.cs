using System.Collections.ObjectModel;
using System.Linq;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.DependencyInjection;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 卡片交互命令（克隆搜索、打开详情、循环状态、删除、星级变更）
/// </summary>
public partial class SearchViewModel
{
    // ==================== 卡片交互 ====================

    /// <summary>
    /// 克隆当前搜索条件到新窗口
    /// 复制：日期范围、包含条件、排除条件（不自动执行搜索）
    /// </summary>
    [RelayCommand]
    private void CloneSearch()
    {
        // 创建新的 ViewModel
        var cloneVm = new SearchViewModel(_gameRepo, _aggregator, _updater, _services);

        // 复制日期范围条件
        cloneVm.SelectedDatePreset = SelectedDatePreset;
        cloneVm.CurrentDateRange = new DateRange(CurrentDateRange.StartMonth, CurrentDateRange.EndMonth);
        cloneVm.IsDateRangeEnabled = IsDateRangeEnabled;

        // 深拷贝包含/排除条件
        cloneVm.IncludeOpts = new ObservableCollection<SearchOpt>(
            IncludeOpts.Select(o => new SearchOpt(o.Key, o.Value, o.Enabled, o.Filter)));
        cloneVm.ExcludeOpts = new ObservableCollection<SearchOpt>(
            ExcludeOpts.Select(o => new SearchOpt(o.Key, o.Value, o.Enabled, o.Filter)));

        // 创建新窗口并显示
        var window = new SearchWindow(cloneVm);
        window.Show();
    }

    /// <summary>
    /// 打开游戏详情窗口。列表查询是投影查询（不含角色/截图/Story 等），
    /// 先 SetGame 立即显示基本信息，再异步加载完整文档补全详情字段。
    /// </summary>
    [RelayCommand]
    private void OpenDetail(Game game)
    {
        var vm = _services.GetRequiredService<GameDetailViewModel>();
        vm.SetGame(game);
        var window = _services.GetRequiredService<GameDetailWindow>();
        window.DataContext = vm;
        window.Show();
        _ = vm.LoadAsync(game.Id);
    }

    /// <summary>循环切换卡片游玩状态</summary>
    [RelayCommand]
    private async Task CycleCardPlayStateAsync(Game game)
    {
        var next = game.PlayState.Next();
        await _updater.UpdatePlayStateAsync(game.Id, next);
        game.PlayState = next;
    }

    /// <summary>循环切换卡片情感状态</summary>
    [RelayCommand]
    private async Task CycleCardEmotionAsync(Game game)
    {
        var next = game.Emotion.Next();
        await _updater.UpdateEmotionAsync(game.Id, next);
        game.Emotion = next;
    }

    /// <summary>切换卡片存储位置</summary>
    [RelayCommand]
    private async Task ToggleCardLocationAsync(Game game)
    {
        var next = game.Location == Location.LOCAL ? Location.REMOTE : Location.LOCAL;
        await _updater.UpdateLocationAsync(game.Id, next);
        game.Location = next;
    }

    /// <summary>删除卡片游戏，并重新加载列表（不触发聚合）</summary>
    [RelayCommand]
    private async Task DeleteCardAsync(Game game)
    {
        await _gameRepo.DeleteGameAsync(game.Id);
        var filter = BuildFilter();
        if (IsGrouping)
            await LoadGroupedDataAsync(filter);
        else
        {
            Games.Remove(game);
            await LoadPagedDataAsync(filter);
        }
    }

    /// <summary>
    /// 卡片星级变更命令（由 GameCardControl 的 StarChangedCommand DP 绑定）
    /// </summary>
    [RelayCommand]
    private async Task CardStarChangedAsync((int GameId, int Star) args)
    {
        await _updater.UpdateStarAsync(args.GameId, args.Star);
    }
}
