using CommunityToolkit.Mvvm.Input;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 标题编辑命令
/// </summary>
public partial class GameDetailViewModel
{
    /// <summary>显示编辑弹窗</summary>
    [RelayCommand]
    private void ShowEditModal()
    {
        IsEditModalVisible = true;
    }

    /// <summary>保存标题修改</summary>
    [RelayCommand]
    private async Task UpdateTitleAsync()
    {
        if (Game == null) return;
        await _updater.UpdateTitleAsync(Game.Id, Game.MainTitle, Game.SubTitle);
        OnPropertyChanged(nameof(WindowTitle));
        IsEditModalVisible = false;
    }

    /// <summary>从原始名称中按空格拆分主标题和副标题</summary>
    [RelayCommand]
    private void SplitTitle()
    {
        if (Game == null) return;
        var spIndex = Game.Name.IndexOf(' ');
        if (spIndex != -1)
        {
            Game.MainTitle = Game.Name.Substring(0, spIndex);
            Game.SubTitle = Game.Name.Substring(spIndex + 1);
        }
    }
}
