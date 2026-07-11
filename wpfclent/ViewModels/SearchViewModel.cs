using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using MongoDB.Driver;
using System.Windows.Data;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 搜索主视图模型 - 管理游戏列表、搜索条件、聚合分析和卡片交互
/// 是整个 WPF 应用的核心 ViewModel
/// </summary>
public partial class SearchViewModel : ObservableObject
{
    private readonly GameRepository _gameRepo;
    private readonly GameAggregator _aggregator;
    private readonly GameUpdater _updater;
    private readonly IServiceProvider _services;

    // 列表查询取消令牌（翻页/分组切换时取消上一次未完成的查询）
    private CancellationTokenSource? _listCts;
    // 聚合查询取消令牌（连点搜索按钮时取消上一次未完成的聚合）
    private CancellationTokenSource? _aggCts;

    /// <summary>取消并释放旧的 CTS，返回新实例的 Token（CTS 内部持有 Timer 等资源，需显式 Dispose）</summary>
    private static CancellationToken RenewCts(ref CancellationTokenSource? cts)
    {
        cts?.Cancel();
        cts?.Dispose();
        return (cts = new CancellationTokenSource()).Token;
    }

    // ==================== 选项列表（供 UI 绑定，定义已移至 SearchOptions） ====================

    /// <summary>搜索类型下拉列表（转发到 SearchOptions，保持 VM 绑定路径兼容）</summary>
    public static List<SearchOptions.SearchTypeOption> SearchTypeOptions => SearchOptions.SearchTypeOptions;
    /// <summary>情感下拉选项</summary>
    public static List<SearchOptions.EnumOption> EmotionOptions => SearchOptions.EmotionOptions;
    /// <summary>位置下拉选项</summary>
    public static List<SearchOptions.EnumOption> LocationOptions => SearchOptions.LocationOptions;
    /// <summary>游玩状态下拉选项</summary>
    public static List<SearchOptions.EnumOption> PlayStateOptions => SearchOptions.PlayStateOptions;
    /// <summary>列表分组类型下拉选项</summary>
    public static List<SearchOptions.GroupTypeOption> GroupTypeOptions => SearchOptions.GroupTypeOptions;
    /// <summary>日期范围预设列表</summary>
    public static List<DatePreset> DatePresets => SearchOptions.DatePresets;

    // ==================== 游戏列表 / 分页 ====================

    /// <summary>当前页的游戏卡片数据</summary>
    [ObservableProperty]
    private ObservableCollection<Game> _games = new();

    /// <summary>当前页码</summary>
    [ObservableProperty]
    private int _page = 1;

    /// <summary>每页显示数量</summary>
    [ObservableProperty]
    private int _pageItemSize = 40;

    /// <summary>符合条件的游戏总数</summary>
    [ObservableProperty]
    private long _total;

    /// <summary>总页数</summary>
    [ObservableProperty]
    private int _totalPages;

    /// <summary>是否正在加载</summary>
    [ObservableProperty]
    private bool _isLoading;

    /// <summary>是否已点击过搜索按钮</summary>
    [ObservableProperty]
    private bool _hasSearched;

    // ==================== 构造函数 ====================

    public SearchViewModel(GameRepository gameRepo, GameAggregator aggregator, GameUpdater updater, IServiceProvider services)
    {
        _gameRepo = gameRepo;
        _aggregator = aggregator;
        _updater = updater;
        _services = services;
    }

    // ==================== 核心逻辑 ====================

    /// <summary>
    /// 分页加载游戏列表 + 总数（不含聚合）
    /// </summary>
    private async Task LoadPagedDataAsync(FilterDefinition<Game>? filter, CancellationToken cancellationToken = default)
    {
        var gamesTask = _gameRepo.QueryGamesAsync(filter, Page, PageItemSize, cancellationToken);
        var countTask = _gameRepo.CountAsync(filter, cancellationToken);
        await Task.WhenAll(gamesTask, countTask);

        Games = new ObservableCollection<Game>(gamesTask.Result);
        // 清除分组（从分组模式切回分页模式时）
        CollectionViewSource.GetDefaultView(Games).GroupDescriptions.Clear();
        Total = countTask.Result;
        TotalPages = Total > 0 ? (int)Math.Ceiling((double)Total / PageItemSize) : 1;
        if (Page > TotalPages) Page = TotalPages;
    }

    /// <summary>
    /// 全量加载游戏并设置 CollectionView 分组（不含聚合，不分页）
    /// 配合虚拟化 ListBox 实现分组模式的整体虚拟化
    /// </summary>
    private async Task LoadGroupedDataAsync(FilterDefinition<Game>? filter, CancellationToken cancellationToken = default)
    {
        var allGames = await _gameRepo.QueryAllGamesAsync(filter, cancellationToken);
        Total = allGames.Count;
        TotalPages = 1;

        // 扁平集合 + CollectionView 分组（配合虚拟化 ListBox）
        Games = new ObservableCollection<Game>(allGames);
        var view = CollectionViewSource.GetDefaultView(Games);
        view.GroupDescriptions.Clear();
        var sortWeights = GameGroupDescription.ComputeSortWeights(allGames, SelectedGroupType);
        view.GroupDescriptions.Add(new GameGroupDescription(SelectedGroupType, sortWeights));
    }

    /// <summary>
    /// 加载右侧聚合面板全部数据（与列表完全独立）
    /// </summary>
    private async Task LoadAggregationsAsync(FilterDefinition<Game>? filter, CancellationToken cancellationToken = default)
    {
        var emotionTask = _aggregator.GroupByEmotionAsync(filter, cancellationToken);
        var locationTask = _aggregator.GroupByLocationAsync(filter, cancellationToken);
        var playStateTask = _aggregator.GroupByPlayStateAsync(filter, cancellationToken);
        var starTask = _aggregator.GroupByStarAsync(filter, cancellationToken);
        var cvTask = _aggregator.GroupByCVAsync(filter, cancellationToken);
        var trueCVTask = _aggregator.GroupByTrueCVAsync(filter, cancellationToken);
        var tagTask = _aggregator.GroupByTagAsync(filter, cancellationToken);
        var dateTask = _aggregator.GroupByDateAsync(filter, cancellationToken);
        var brandTask = _aggregator.GroupByBrandAsync(filter, cancellationToken);

        await Task.WhenAll(emotionTask, locationTask, playStateTask,
            starTask, cvTask, trueCVTask, tagTask, dateTask, brandTask);

        EmotionGroup = new ObservableCollection<EmotionGroupItem>(emotionTask.Result);
        LocationGroup = new ObservableCollection<LocationGroupItem>(locationTask.Result);
        PlayStateGroup = new ObservableCollection<PlayStateGroupItem>(playStateTask.Result);
        StarGroup = new ObservableCollection<StarGroupItem>(starTask.Result);
        CvGroup = new ObservableCollection<GroupItem>(cvTask.Result);
        TrueCVGroup = new ObservableCollection<GroupItem>(trueCVTask.Result);
        TagGroup = new ObservableCollection<GroupItem>(tagTask.Result);
        DateGroup = new ObservableCollection<DateGroupItem>(dateTask.Result);
        BrandGroup = new ObservableCollection<BrandGroupItem>(brandTask.Result);
    }

    /// <summary>
    /// 构建 MongoDB 强类型查询条件（委托到 SearchFilterBuilder）
    /// </summary>
    private FilterDefinition<Game>? BuildFilter()
        => SearchFilterBuilder.Build(IncludeOpts, ExcludeOpts, IsDateRangeEnabled, CurrentDateRange);
}
