using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 角色 CV 编辑命令
/// </summary>
public partial class GameDetailViewModel
{
    /// <summary>更新指定角色的声优信息</summary>
    [RelayCommand]
    private async Task UpdateCharCVAsync(GameCharacter character)
    {
        if (Game == null) return;
        await _updater.SetCharCVAsync(Game.Id, character.Index, character.Cv);
    }

    // ==================== 角色编辑弹窗 ====================

    /// <summary>当前正在编辑的角色</summary>
    [ObservableProperty]
    private GameCharacter? _selectedCharacter;

    /// <summary>是否显示角色编辑弹窗</summary>
    [ObservableProperty]
    private bool _isCharEditVisible;

    /// <summary>显示角色编辑弹窗</summary>
    [RelayCommand]
    private void ShowCharEdit(GameCharacter? character)
    {
        SelectedCharacter = character;
        IsCharEditVisible = true;
    }

    /// <summary>关闭角色编辑弹窗</summary>
    [RelayCommand]
    private void CloseCharEdit()
    {
        IsCharEditVisible = false;
        SelectedCharacter = null;
    }

    /// <summary>从弹窗更新声优</summary>
    [RelayCommand]
    private async Task UpdateCharCVFromModalAsync()
    {
        if (Game == null || SelectedCharacter == null) return;
        await _updater.SetCharCVAsync(Game.Id, SelectedCharacter.Index, SelectedCharacter.Cv);
        IsCharEditVisible = false;
    }

    /// <summary>将角色标记为男主角（SetMan）</summary>
    [RelayCommand]
    private async Task SetCharManAsync(GameCharacter? character)
    {
        if (Game == null || character == null) return;
        await _updater.SetCharManAsync(Game.Id, character.Index);
        character.Man = true;
        OnPropertyChanged(nameof(Heroines));
        OnPropertyChanged(nameof(Protagonists));
        IsCharEditVisible = false;
    }
}
