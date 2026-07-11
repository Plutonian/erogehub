using System.Collections.ObjectModel;
using System.Linq.Expressions;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.DependencyInjection;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Linq;
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

    // ==================== 列表分组 ====================

    /// <summary>当前选中的分组类型</summary>
    [ObservableProperty]
    private GroupType _selectedGroupType = GroupType.None;

    /// <summary>是否处于分组模式</summary>
    public bool IsGrouping => SelectedGroupType != GroupType.None;

    /// <summary>是否处于非分组模式（用于 UI 可见性反转）</summary>
    public bool IsNotGrouping => SelectedGroupType == GroupType.None;

    // ==================== 搜索条件 ====================

    /// <summary>当前选中的日期预设 key</summary>
    [ObservableProperty]
    private string _selectedDatePreset = "THIS_MONTH";

    /// <summary>是否启用日期范围筛选</summary>
    [ObservableProperty]
    private bool _isDateRangeEnabled = true;

    /// <summary>当前日期范围</summary>
    [ObservableProperty]
    private DateRange _currentDateRange = DateRangeHelper.GetDateRange("THIS_MONTH");

    /// <summary>包含条件列表（满足才显示）</summary>
    [ObservableProperty]
    private ObservableCollection<SearchOpt> _includeOpts = new();

    /// <summary>排除条件列表（满足则隐藏）</summary>
    [ObservableProperty]
    private ObservableCollection<SearchOpt> _excludeOpts = new();

    /// <summary>是否选择了"自定义"日期预设</summary>
    public bool IsCustomDatePreset => SelectedDatePreset == "CUSTOM";

    /// <summary>日期范围显示文本</summary>
    public string DateRangeDisplay => $"{CurrentDateRange.StartMonth} ~ {CurrentDateRange.EndMonth}";

    /// <summary>日期预设变更时同步日期范围，自定义模式不清除已有范围</summary>
    partial void OnSelectedDatePresetChanged(string value)
    {
        OnPropertyChanged(nameof(IsCustomDatePreset));
        if (value != "CUSTOM")
        {
            CurrentDateRange = DateRangeHelper.GetDateRange(value);
        }
    }

    partial void OnCurrentDateRangeChanged(DateRange value)
    {
        OnPropertyChanged(nameof(DateRangeDisplay));
    }

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

    // ==================== 聚合数据 ====================

    /// <summary>情感聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<EmotionGroupItem> _emotionGroup = new();

    /// <summary>星级聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<StarGroupItem> _starGroup = new();

    /// <summary>位置聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<LocationGroupItem> _locationGroup = new();

    /// <summary>游玩状态聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<PlayStateGroupItem> _playStateGroup = new();

    /// <summary>声优聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<GroupItem> _cvGroup = new();

    /// <summary>去马甲CV聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<GroupItem> _trueCVGroup = new();

    /// <summary>标签聚合数据</summary>
    [ObservableProperty]
    private ObservableCollection<GroupItem> _tagGroup = new();

    /// <summary>日期聚合数据（年→月树形结构）</summary>
    [ObservableProperty]
    private ObservableCollection<DateGroupItem> _dateGroup = new();

    /// <summary>品牌聚合数据（公司→品牌树形结构）</summary>
    [ObservableProperty]
    private ObservableCollection<BrandGroupItem> _brandGroup = new();

    // ==================== 聚合面板选中项（双向绑定，选中后触发命令并清空，允许重复点击） ====================

    [ObservableProperty]
    private StarGroupItem? _selectedStar;
    partial void OnSelectedStarChanged(StarGroupItem? value)
    {
        if (value is null) return;
        OnStarClicked(value);
        SelectedStar = null;
    }

    [ObservableProperty]
    private EmotionGroupItem? _selectedEmotion;
    partial void OnSelectedEmotionChanged(EmotionGroupItem? value)
    {
        if (value is null) return;
        OnEmotionClicked(value);
        SelectedEmotion = null;
    }

    [ObservableProperty]
    private LocationGroupItem? _selectedLocation;
    partial void OnSelectedLocationChanged(LocationGroupItem? value)
    {
        if (value is null) return;
        OnLocationClicked(value);
        SelectedLocation = null;
    }

    [ObservableProperty]
    private PlayStateGroupItem? _selectedPlayState;
    partial void OnSelectedPlayStateChanged(PlayStateGroupItem? value)
    {
        if (value is null) return;
        OnPlayStateClicked(value);
        SelectedPlayState = null;
    }

    // ==================== 构造函数 ====================

    public SearchViewModel(GameRepository gameRepo, GameAggregator aggregator, GameUpdater updater, IServiceProvider services)
    {
        _gameRepo = gameRepo;
        _aggregator = aggregator;
        _updater = updater;
        _services = services;
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

    /// <summary>添加一条搜索条件</summary>
    [RelayCommand]
    private void AddSearchOpt(string filterType)
    {
        var f = Enum.Parse<FilterType>(filterType);
        var opt = new SearchOpt(SearchType.TAG, "", true, f);
        if (f == FilterType.Include)
            IncludeOpts.Add(opt);
        else
            ExcludeOpts.Add(opt);
    }

    /// <summary>删除一条搜索条件</summary>
    [RelayCommand]
    private void RemoveSearchOpt(SearchOpt opt)
    {
        if (opt.Filter == FilterType.Include)
            IncludeOpts.Remove(opt);
        else
            ExcludeOpts.Remove(opt);
    }

    /// <summary>交换搜索条件的包含/排除类型</summary>
    [RelayCommand]
    private void SwapSearchOpt(SearchOpt opt)
    {
        if (opt.Filter == FilterType.Include)
        {
            IncludeOpts.Remove(opt);
            opt.Filter = FilterType.Exclude;
            ExcludeOpts.Add(opt);
        }
        else
        {
            ExcludeOpts.Remove(opt);
            opt.Filter = FilterType.Include;
            IncludeOpts.Add(opt);
        }
    }

    /// <summary>快捷调整日期范围（+/- 月或年，或跳转到本月）</summary>
    [RelayCommand]
    private void AdjustDateRange(string direction)
    {
        if (direction == "this_month")
        {
            SelectedDatePreset = "THIS_MONTH";
            CurrentDateRange = DateRangeHelper.GetDateRange("THIS_MONTH");
            return;
        }

        var (delta, isYear) = direction switch
        {
            "+1y" => (1, true),
            "-1y" => (-1, true),
            "+1m" => (1, false),
            "-1m" => (-1, false),
            _ => (0, false),
        };

        if (delta != 0)
        {
            SelectedDatePreset = "CUSTOM";
            CurrentDateRange = DateRangeHelper.AdjustDateRange(CurrentDateRange, delta, isYear);
        }
    }

    // ==================== 聚合面板点击 ====================

    /// <summary>点击情感聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnEmotionClicked(EmotionGroupItem item)
        => AddAsIncludeOpt(SearchType.EMOTION, ((int)item.Emotion).ToString());

    /// <summary>点击星级聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnStarClicked(StarGroupItem item)
        => AddAsIncludeOpt(SearchType.STAR, item.Star.ToString());

    /// <summary>点击位置聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnLocationClicked(LocationGroupItem item)
        => AddAsIncludeOpt(SearchType.LOCATION, ((int)item.Location).ToString());

    /// <summary>点击游玩状态聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnPlayStateClicked(PlayStateGroupItem item)
        => AddAsIncludeOpt(SearchType.PLAYSTATE, ((int)item.PlayState).ToString());

    /// <summary>点击声优聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnCVClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.CV, item.Title);

    /// <summary>点击去马甲CV聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnTrueCVClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.TRUECV, item.Title);

    /// <summary>点击标签聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnTagClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.TAG, item.Title);

    /// <summary>点击品牌聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnBrandClicked(BrandGroupItem item)
        => AddAsIncludeOpt(SearchType.BRAND, item.Title);

    /// <summary>点击游戏卡片上的标签，添加为包含条件</summary>
    [RelayCommand]
    private void TagClickToSearch(string tag)
        => AddAsIncludeOpt(SearchType.TAG, tag);

    /// <summary>点击游戏卡片上的品牌，添加为包含条件</summary>
    [RelayCommand]
    private void BrandClickToSearch(string brand)
        => AddAsIncludeOpt(SearchType.BRAND, brand);

    /// <summary>点击日期聚合项，切换到自定义日期范围</summary>
    [RelayCommand]
    private void OnDateClicked(DateGroupItem item)
    {
        SelectedDatePreset = "CUSTOM";
        CurrentDateRange = new(DateRangeHelper.FormatMonth(item.Start), DateRangeHelper.FormatMonth(item.End));
    }

    /// <summary>添加一条包含条件的快捷方法</summary>
    private void AddAsIncludeOpt(SearchType type, string value)
    {
        IncludeOpts.Add(new SearchOpt(type, value, true, FilterType.Include));
    }

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

    /// <summary>打开游戏详情窗口</summary>
    [RelayCommand]
    private void OpenDetail(Game game)
    {
        var vm = _services.GetRequiredService<GameDetailViewModel>();
        vm.SetGame(game);
        var window = _services.GetRequiredService<GameDetailWindow>();
        window.DataContext = vm;
        window.Show();
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
