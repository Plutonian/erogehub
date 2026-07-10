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

    // ==================== 选项列表（供 UI 绑定） ====================

    /// <summary>搜索类型选项记录</summary>
    public record SearchTypeOption(string Label, SearchType Type);

    /// <summary>搜索类型下拉列表（左侧面板条件类型选择）</summary>
    public static List<SearchTypeOption> SearchTypeOptions { get; } = new()
    {
        new("全文检索", SearchType.TEXT),
        new("Regex", SearchType.REGEX),
        new("Tag", SearchType.TAG),
        new("星级", SearchType.STAR),
        new("情感", SearchType.EMOTION),
        new("位置", SearchType.LOCATION),
        new("游玩状态", SearchType.PLAYSTATE),
        new("画师", SearchType.PAINTER),
        new("剧本作家", SearchType.WRITER),
        new("声优", SearchType.CV),
        new("去马甲CV", SearchType.TRUECV),
        new("品牌", SearchType.BRAND),
    };

    /// <summary>枚举选项记录（值 + 显示名）</summary>
    public record EnumOption(string Label, string Value);

    /// <summary>情感下拉选项</summary>
    public static List<EnumOption> EmotionOptions { get; } = new()
    {
        new(Emotion.LIKE.Label(), ((int)Emotion.LIKE).ToString()),
        new(Emotion.HOPE.Label(), ((int)Emotion.HOPE).ToString()),
        new(Emotion.NORMAL.Label(), ((int)Emotion.NORMAL).ToString()),
        new(Emotion.HATE.Label(), ((int)Emotion.HATE).ToString()),
    };

    /// <summary>位置下拉选项</summary>
    public static List<EnumOption> LocationOptions { get; } = new()
    {
        new("Local", ((int)Location.LOCAL).ToString()),
        new("Remote", ((int)Location.REMOTE).ToString()),
    };

    /// <summary>游玩状态下拉选项</summary>
    public static List<EnumOption> PlayStateOptions { get; } = new()
    {
        new(PlayState.NOT_PLAY.Label(), ((int)PlayState.NOT_PLAY).ToString()),
        new(PlayState.PLAYING.Label(), ((int)PlayState.PLAYING).ToString()),
        new(PlayState.PLAYED.Label(), ((int)PlayState.PLAYED).ToString()),
    };

    /// <summary>分组类型选项记录</summary>
    public record GroupTypeOption(string Label, GroupType Type);

    /// <summary>列表分组类型下拉选项（底部栏分组选择器）</summary>
    public static List<GroupTypeOption> GroupTypeOptions { get; } = new()
    {
        new("无分组", GroupType.None),
        new("年", GroupType.Year),
        new("月", GroupType.Month),
        new("星级", GroupType.Star),
        new("情感", GroupType.Emotion),
        new("位置", GroupType.Location),
        new("游玩状态", GroupType.PlayState),
        new("品牌", GroupType.Brand),
    };

    /// <summary>日期范围预设列表</summary>
    public static List<DatePreset> DatePresets => DateRangeHelper.Presets;

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
        IsLoading = true;
        try
        {
            var filter = BuildFilter();
            if (value == GroupType.None)
                await LoadPagedDataAsync(filter);
            else
                await LoadGroupedDataAsync(filter);
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
        HasSearched = true;
        Page = 1;
        IsLoading = true;
        try
        {
            var filter = BuildFilter();
            // 并行：列表数据 + 右侧聚合
            var dataTask = IsGrouping
                ? LoadGroupedDataAsync(filter)
                : LoadPagedDataAsync(filter);
            var aggTask = LoadAggregationsAsync(filter);
            await Task.WhenAll(dataTask, aggTask);
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
        Page = page;
        IsLoading = true;
        try
        {
            await LoadPagedDataAsync(BuildFilter());
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
        => AddAsIncludeOpt(SearchType.EMOTION, item.Emotion.ToString());

    /// <summary>点击星级聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnStarClicked(StarGroupItem item)
        => AddAsIncludeOpt(SearchType.STAR, item.Star.ToString());

    /// <summary>点击位置聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnLocationClicked(LocationGroupItem item)
        => AddAsIncludeOpt(SearchType.LOCATION, item.Location.ToString());

    /// <summary>点击游玩状态聚合项，添加为包含条件</summary>
    [RelayCommand]
    private void OnPlayStateClicked(PlayStateGroupItem item)
        => AddAsIncludeOpt(SearchType.PLAYSTATE, item.PlayState.ToString());

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

    /// <summary>
    /// 卡片星级修改（保留供其他窗口直接调用；卡片交互请使用 CardStarChangedCommand）
    /// </summary>
    public async Task UpdateCardStarAsync(int gameId, int star)
    {
        await _updater.UpdateStarAsync(gameId, star);
    }

    /// <summary>
    /// 卡片标记相同（保留供其他窗口直接调用；卡片交互请使用 CardMarkSameChangedCommand）
    /// </summary>
    public async Task UpdateCardMarkSameAsync(int gameId, bool isSame)
    {
        await _updater.MarkSameAsync(gameId, isSame);
    }

    // ==================== 核心逻辑 ====================

    /// <summary>
    /// 分页加载游戏列表 + 总数（不含聚合）
    /// </summary>
    private async Task LoadPagedDataAsync(FilterDefinition<Game>? filter)
    {
        var gamesTask = _gameRepo.QueryGamesAsync(filter, Page, PageItemSize);
        var countTask = _gameRepo.CountAsync(filter);
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
    private async Task LoadGroupedDataAsync(FilterDefinition<Game>? filter)
    {
        var allGames = await _gameRepo.QueryAllGamesAsync(filter);
        Total = allGames.Count;
        TotalPages = 1;

        // 扁平集合 + CollectionView 分组（配合虚拟化 ListBox）
        Games = new ObservableCollection<Game>(allGames);
        var view = CollectionViewSource.GetDefaultView(Games);
        view.GroupDescriptions.Clear();
        var sortWeights = ComputeGroupSortWeights(allGames, SelectedGroupType);
        view.GroupDescriptions.Add(new GameGroupDescription(SelectedGroupType, sortWeights));
    }

    /// <summary>
    /// 加载右侧聚合面板全部数据（与列表完全独立）
    /// </summary>
    private async Task LoadAggregationsAsync(FilterDefinition<Game>? filter)
    {
        var emotionTask = _aggregator.GroupByEmotionAsync(filter);
        var locationTask = _aggregator.GroupByLocationAsync(filter);
        var playStateTask = _aggregator.GroupByPlayStateAsync(filter);
        var starTask = _aggregator.GroupByStarAsync(filter);
        var cvTask = _aggregator.GroupByCVAsync(filter);
        var trueCVTask = _aggregator.GroupByTrueCVAsync(filter);
        var tagTask = _aggregator.GroupByTagAsync(filter);
        var dateTask = _aggregator.GroupByDateAsync(filter);
        var brandTask = _aggregator.GroupByBrandAsync(filter);

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

    /// <summary>预计算各分组的排序权重（升序，值越小排越前），供 GameGroupDescription 控制组顺序</summary>
    private static Dictionary<string, double> ComputeGroupSortWeights(List<Game> games, GroupType type)
    {
        return type switch
        {
            GroupType.Year => games
                .Select(g => g.PublishDate.Year > 1 ? g.PublishDate.Year : 0)
                .Distinct()
                .OrderByDescending(y => y)
                .Select((y, i) => (key: y.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Month => games
                .Select(g => g.PublishDate.Year > 1 ? g.PublishDate.ToString("yyyy-MM") : "0000-00")
                .Distinct()
                .OrderByDescending(s => s)
                .Select((s, i) => (key: s, rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Star => games
                .Select(g => g.Star)
                .Distinct()
                .OrderByDescending(s => s)
                .Select((s, i) => (key: s.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Emotion => games
                .Select(g => g.Emotion)
                .Distinct()
                .OrderByDescending(e => e)
                .Select((e, i) => (key: e.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Location => games
                .Select(g => g.Location)
                .Distinct()
                .OrderByDescending(l => l)
                .Select((l, i) => (key: l.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.PlayState => games
                .Select(g => g.PlayState)
                .Distinct()
                .OrderByDescending(p => p)
                .Select((p, i) => (key: p.ToString(), rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            GroupType.Brand => games
                .Select(g => g.Brand != null && !string.IsNullOrEmpty(g.Brand.Comp) ? g.Brand.Comp
                            : g.Brand != null && !string.IsNullOrEmpty(g.Brand.Name) ? g.Brand.Name
                            : "无品牌")
                .GroupBy(name => name)
                .OrderByDescending(g => g.Count())
                .Select((g, i) => (key: g.Key, rank: (double)i))
                .ToDictionary(x => x.key, x => x.rank),

            _ => new Dictionary<string, double>()
        };
    }

    /// <summary>
    /// 构建 MongoDB 强类型查询条件 FilterDefinition&lt;Game&gt;
    /// 将 UI 中的搜索条件（包含/排除 + 日期范围）转为 MongoDB 查询语法。
    /// 使用 Builders&lt;Game&gt;.Filter 强类型构造，字段名重命名时编译器会报错。
    /// </summary>
    private FilterDefinition<Game>? BuildFilter()
    {
        // 收集所有已启用且有值的搜索条件
        var allOpts = IncludeOpts.Concat(ExcludeOpts)
            .Where(o => o.Enabled && !string.IsNullOrWhiteSpace(o.Value))
            .ToList();

        var hasDateRange = IsDateRangeEnabled &&
            (!string.IsNullOrEmpty(CurrentDateRange.StartMonth) || !string.IsNullOrEmpty(CurrentDateRange.EndMonth));

        // 无任何条件时返回 null（查询全部）
        if (allOpts.Count == 0 && !hasDateRange)
            return null;

        var filters = new List<FilterDefinition<Game>>();
        var fb = Builders<Game>.Filter;

        // 遍历搜索条件，构建对应的强类型过滤器
        foreach (var opt in allOpts)
        {
            var isExclude = opt.Filter == FilterType.Exclude;
            var value = opt.Value.Trim();
            // 公共：标量字段正则匹配，exclude 时用 $not 包裹
            FilterDefinition<Game> RegexMatch(Expression<Func<Game, object?>> field, string pattern)
            {
                var regex = fb.Regex(field, new BsonRegularExpression(pattern));
                return isExclude ? fb.Not(regex) : regex;
            }
            // 公共：字符串路径正则匹配（用于数组/嵌套字段）
            FilterDefinition<Game> RegexMatchPath(string fieldPath, string pattern)
            {
                var regex = fb.Regex(fieldPath, new BsonRegularExpression(pattern));
                return isExclude ? fb.Not(regex) : regex;
            }
            // 公共：强类型字段，include 等于 / exclude 不等于
            FilterDefinition<Game> Eq<T>(Expression<Func<Game, T>> field, T v)
                => isExclude ? fb.Ne(field, v) : fb.Eq(field, v);
            // 公共：字符串路径等于（用于数组字段，MongoDB 对数组做 Eq 会自动匹配元素）
            FilterDefinition<Game> StringEqPath(string fieldPath, string v)
                => isExclude ? fb.Ne(fieldPath, v) : fb.Eq(fieldPath, v);

            switch (opt.Key)
            {
                case SearchType.REGEX:
                    // name 字段正则匹配
                    filters.Add(RegexMatch(g => g.Name, value));
                    break;
                case SearchType.TEXT:
                {
                    // 全文检索（MongoDB $text 索引）。$text 不支持 $not，exclude 时用 $nor 取反
                    if (isExclude)
                        filters.Add(new BsonDocument("$nor", new BsonArray { new BsonDocument("$text", new BsonDocument("$search", value)) }));
                    else
                        filters.Add(fb.Text(value));
                    break;
                }
                case SearchType.TAG:
                    // tag 数组正则匹配（MongoDB 对数组字段做 Regex 会自动匹配任意元素）
                    filters.Add(RegexMatchPath("tag", value));
                    break;
                case SearchType.STAR:
                    if (int.TryParse(value, out var starValue))
                        filters.Add(Eq(g => g.Star, starValue));
                    break;
                case SearchType.EMOTION:
                    if (int.TryParse(value, out var emotionVal))
                        filters.Add(Eq(g => g.Emotion, (Emotion)emotionVal));
                    break;
                case SearchType.LOCATION:
                    if (int.TryParse(value, out var locationVal))
                        filters.Add(Eq(g => g.Location, (Location)locationVal));
                    break;
                case SearchType.PLAYSTATE:
                    if (int.TryParse(value, out var playStateVal))
                        filters.Add(Eq(g => g.PlayState, (PlayState)playStateVal));
                    break;
                case SearchType.PAINTER:
                    // painter 是 List<string>，用字符串路径做 Eq，MongoDB 会自动匹配数组元素
                    filters.Add(StringEqPath("painter", value));
                    break;
                case SearchType.WRITER:
                    filters.Add(StringEqPath("writer", value));
                    break;
                case SearchType.CV:
                    // 嵌套数组字段正则：用字符串路径访问 gamechar.cv
                    filters.Add(RegexMatchPath("gamechar.cv", value));
                    break;
                case SearchType.TRUECV:
                    filters.Add(RegexMatchPath("gamechar.truecv", value));
                    break;
                case SearchType.BRAND:
                    // 嵌套子文档字段正则：用字符串路径访问 brand.name
                    filters.Add(RegexMatchPath("brand.name", value));
                    break;
            }
        }

        // 添加日期范围查询条件（半开区间 [start, end)）
        if (hasDateRange)
        {
            var dateFilters = new List<FilterDefinition<Game>>();

            if (!string.IsNullOrEmpty(CurrentDateRange.StartMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(CurrentDateRange.StartMonth);
                var startLocal = new DateTime(year, month, 1, 0, 0, 0, DateTimeKind.Local);
                dateFilters.Add(fb.Gte(g => g.PublishDate, startLocal.ToUniversalTime()));
            }

            if (!string.IsNullOrEmpty(CurrentDateRange.EndMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(CurrentDateRange.EndMonth);
                // endDate 为下个月1日（半开区间 [start, end)）
                var endLocal = new DateTime(year, month, 1, 0, 0, 0, DateTimeKind.Local).AddMonths(1);
                dateFilters.Add(fb.Lt(g => g.PublishDate, endLocal.ToUniversalTime()));
            }

            if (dateFilters.Count > 0)
                filters.Add(fb.And(dateFilters));
        }

        if (filters.Count == 0)
            return null;

        return fb.And(filters);
    }
}
