using System.Diagnostics;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 游戏详情视图模型 - 负责单个游戏的查看和编辑逻辑
/// 包括：快速修改状态、Tag 管理、标题编辑、角色 CV 修改、外部搜索跳转
/// </summary>
public partial class GameDetailViewModel : ObservableObject
{
    private readonly GameRepository _gameRepo;
    private readonly GameUpdater _updater;

    /// <summary>当前查看的游戏对象</summary>
    [ObservableProperty]
    [NotifyPropertyChangedFor(nameof(WindowTitle))]
    private Game? _game;

    /// <summary>窗口标题：MainTitle + SubTitle</summary>
    public string WindowTitle
    {
        get
        {
            if (Game == null) return "Game Detail";
            var title = Game.MainTitle;
            if (!string.IsNullOrEmpty(Game.SubTitle))
                title += " " + Game.SubTitle;
            return title;
        }
    }

    /// <summary>是否正在加载</summary>
    [ObservableProperty]
    private bool _isLoading;

    /// <summary>新增标签输入框绑定的文本</summary>
    [ObservableProperty]
    private string _newTag = string.Empty;

    /// <summary>是否正在添加标签（控制输入框显隐，仿 web 版）</summary>
    [ObservableProperty]
    private bool _isAddingTag;

    /// <summary>是否显示编辑弹窗</summary>
    [ObservableProperty]
    private bool _isEditModalVisible;

    /// <summary>女主角列表（man=false）</summary>
    public IEnumerable<GameCharacter> Heroines => Game?.GameCharacters.Where(c => !c.Man) ?? Enumerable.Empty<GameCharacter>();

    /// <summary>男主角列表（man=true）</summary>
    public IEnumerable<GameCharacter> Protagonists => Game?.GameCharacters.Where(c => c.Man) ?? Enumerable.Empty<GameCharacter>();

    /// <summary>共享的枚举选项列表（引用 SearchViewModel）</summary>
    public static List<SearchViewModel.EnumOption> EmotionOptions => SearchViewModel.EmotionOptions;
    public static List<SearchViewModel.EnumOption> LocationOptions => SearchViewModel.LocationOptions;
    public static List<SearchViewModel.EnumOption> PlayStateOptions => SearchViewModel.PlayStateOptions;

    public GameDetailViewModel(GameRepository gameRepo, GameUpdater updater)
    {
        _gameRepo = gameRepo;
        _updater = updater;
    }

    /// <summary>Game 变更时刷新角色分类列表和窗口标题</summary>
    partial void OnGameChanged(Game? value)
    {
        OnPropertyChanged(nameof(Heroines));
        OnPropertyChanged(nameof(Protagonists));
        OnPropertyChanged(nameof(WindowTitle));
    }

    /// <summary>
    /// 直接设置 Game 对象（从搜索列表传入，共享引用避免重复加载）
    /// </summary>
    public void SetGame(Game game)
    {
        Game = game;
    }

    /// <summary>
    /// 从数据库重新加载游戏数据
    /// </summary>
    public async Task LoadAsync(int gameId, CancellationToken cancellationToken = default)
    {
        IsLoading = true;
        try
        {
            Game = await _gameRepo.QueryGameByIdAsync(gameId, cancellationToken);
        }
        finally
        {
            IsLoading = false;
        }
    }

    // ==================== 快速修改命令 ====================

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

    // ==================== Tag 操作 ====================

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

    // ==================== 标题编辑 ====================

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

    // ==================== 角色 CV 编辑 ====================

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

    // ==================== 外部链接 ====================

    /// <summary>在默认浏览器中打开 URL</summary>
    [RelayCommand]
    private void OpenUrl(string url)
    {
        if (string.IsNullOrEmpty(url)) return;
        try
        {
            Process.Start(new ProcessStartInfo(url) { UseShellExecute = true });
        }
        catch { }
    }

    /// <summary>在外部网站搜索游戏名</summary>
    [RelayCommand]
    private void OpenExternalSearch(string engine)
    {
        if (Game == null) return;
        var title = Uri.EscapeDataString(Game.MainTitle);
        var url = engine switch
        {
            "google" => $"https://www.google.com/search?q={title}",
            "wiki" => $"https://ja.wikipedia.org/w/index.php?search={title}",
            "getchu" => $"http://www.getchu.com/soft.phtml?id={Game.Id}&gc=gc",
            "dlsite" => $"https://www.dlsite.com/pro/fsr/=/language/jp/sex_category[0]/male/keyword/{title}",
            "erogamescape" => $"https://erogamescape.dyndns.org/~ap2/ero/toukei_kaiseki/kensaku.php?category=game&word_category=name&mode=normal&word={title}",
            "2df" => $"https://galge.fun/subjects/search?keyword={title}",
            "ggbases" => $"https://www.ggbases.com/search.so?p=0&advanced=&title={title}",
            _ => "",
        };
        if (!string.IsNullOrEmpty(url))
            OpenUrl(url);
    }
}
