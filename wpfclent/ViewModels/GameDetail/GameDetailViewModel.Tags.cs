using System.Linq;
using CommunityToolkit.Mvvm.Input;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// Tag 标签管理命令
/// </summary>
public partial class GameDetailViewModel
{
    /// <summary>添加新标签</summary>
    [RelayCommand]
    private async Task AddTagAsync()
    {
        if (Game == null || string.IsNullOrWhiteSpace(NewTag)) return;
        var tags = Game.Tag.ToList();
        tags.Add(NewTag.Trim());
        await _updater.UpdateTagsAsync(Game.Id, tags);
        Game.Tag = tags;
        NewTag = string.Empty;
    }

    /// <summary>移除指定标签</summary>
    [RelayCommand]
    private async Task RemoveTagAsync(string tag)
    {
        if (Game == null) return;
        var tags = Game.Tag.Where(t => t != tag).ToList();
        await _updater.UpdateTagsAsync(Game.Id, tags);
        Game.Tag = tags;
    }
}
