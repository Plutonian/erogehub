using System.Linq;
using CommunityToolkit.Mvvm.ComponentModel;
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

    /// <summary>共享的枚举选项列表（引用 SearchOptions）</summary>
    public static List<SearchOptions.EnumOption> EmotionOptions => SearchOptions.EmotionOptions;
    public static List<SearchOptions.EnumOption> LocationOptions => SearchOptions.LocationOptions;
    public static List<SearchOptions.EnumOption> PlayStateOptions => SearchOptions.PlayStateOptions;

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
}
