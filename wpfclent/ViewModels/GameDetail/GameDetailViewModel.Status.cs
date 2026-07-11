using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 快速修改状态命令（游玩状态、情感、位置、星级、标记相同）
/// </summary>
public partial class GameDetailViewModel
{
    /// <summary>循环切换游玩状态：未玩 → 进行中 → 已通关 → 未玩</summary>
    [RelayCommand]
    private async Task CyclePlayStateAsync()
    {
        if (Game == null) return;
        var next = Game.PlayState.Next();
        await _updater.UpdatePlayStateAsync(Game.Id, next);
        Game.PlayState = next;
    }

    /// <summary>循环切换情感状态：嫌い → ... → 気になり → 好き → 嫌い</summary>
    [RelayCommand]
    private async Task CycleEmotionAsync()
    {
        if (Game == null) return;
        var next = Game.Emotion.Next();
        await _updater.UpdateEmotionAsync(Game.Id, next);
        Game.Emotion = next;
    }

    /// <summary>切换存储位置：本地 ↔ 云端</summary>
    [RelayCommand]
    private async Task ToggleLocationAsync()
    {
        if (Game == null) return;
        var next = Game.Location == Location.LOCAL ? Location.REMOTE : Location.LOCAL;
        await _updater.UpdateLocationAsync(Game.Id, next);
        Game.Location = next;
    }

    /// <summary>更新星级评分（由 UI 事件调用）</summary>
    public async Task UpdateStarAsync(int star)
    {
        if (Game == null) return;
        await _updater.UpdateStarAsync(Game.Id, star);
    }

    /// <summary>切换标记相同状态（由 UI 事件调用）</summary>
    public async Task ToggleMarkSameAsync(bool isSame)
    {
        if (Game == null) return;
        await _updater.MarkSameAsync(Game.Id, isSame);
    }
}
