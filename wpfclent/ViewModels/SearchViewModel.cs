using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.DependencyInjection;
using MongoDB.Bson;
using System.Linq;
using System.Windows.Data;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 搜索主视图模型 - 管理游戏列表、搜索条件、聚合分析和卡片交互
/// 是整个 WPF 应用的核心 ViewModel
/// </summary>
public partial class SearchViewModel : ObservableObject
{
    private readonly MongoService _mongo;
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
        new("好き", ((int)Emotion.LIKE).ToString()),
        new("気になり", ((int)Emotion.HOPE).ToString()),
        new("...", ((int)Emotion.NORMAL).ToString()),
        new("嫌い", ((int)Emotion.HATE).ToString()),
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
        new("...", ((int)PlayState.NOT_PLAY).ToString()),
        new("進行中", ((int)PlayState.PLAYING).ToString()),
        new("プレイ済み", ((int)PlayState.PLAYED).ToString()),
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

    public SearchViewModel(MongoService mongo, IServiceProvider services)
    {
        _mongo = mongo;
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
        var cloneVm = new SearchViewModel(_mongo, _services);

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
        var next = game.PlayState switch
        {
            (int)PlayState.NOT_PLAY => (int)PlayState.PLAYING,
            (int)PlayState.PLAYING => (int)PlayState.PLAYED,
            _ => (int)PlayState.NOT_PLAY,
        };
        await _mongo.UpdatePlayStateAsync(game.Id, next);
        game.PlayState = next;
    }

    /// <summary>循环切换卡片情感状态</summary>
    [RelayCommand]
    private async Task CycleCardEmotionAsync(Game game)
    {
        var next = game.Emotion switch
        {
            (int)Emotion.HATE => (int)Emotion.NORMAL,
            (int)Emotion.NORMAL => (int)Emotion.HOPE,
            (int)Emotion.HOPE => (int)Emotion.LIKE,
            _ => (int)Emotion.HATE,
        };
        await _mongo.UpdateEmotionAsync(game.Id, next);
        game.Emotion = next;
    }

    /// <summary>切换卡片存储位置</summary>
    [RelayCommand]
    private async Task ToggleCardLocationAsync(Game game)
    {
        var next = game.Location == (int)Location.LOCAL ? (int)Location.REMOTE : (int)Location.LOCAL;
        await _mongo.UpdateLocationAsync(game.Id, next);
        game.Location = next;
    }

    /// <summary>删除卡片游戏，并重新加载列表（不触发聚合）</summary>
    [RelayCommand]
    private async Task DeleteCardAsync(Game game)
    {
        await _mongo.DeleteGameAsync(game.Id);
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
        await _mongo.UpdateStarAsync(args.GameId, args.Star);
    }

    /// <summary>
    /// 卡片星级修改（保留供其他窗口直接调用；卡片交互请使用 CardStarChangedCommand）
    /// </summary>
    public async Task UpdateCardStarAsync(int gameId, int star)
    {
        await _mongo.UpdateStarAsync(gameId, star);
    }

    /// <summary>
    /// 卡片标记相同（保留供其他窗口直接调用；卡片交互请使用 CardMarkSameChangedCommand）
    /// </summary>
    public async Task UpdateCardMarkSameAsync(int gameId, bool isSame)
    {
        await _mongo.MarkSameAsync(gameId, isSame);
    }

    // ==================== 核心逻辑 ====================

    /// <summary>
    /// 分页加载游戏列表 + 总数（不含聚合）
    /// </summary>
    private async Task LoadPagedDataAsync(BsonDocument? filter)
    {
        var gamesTask = _mongo.QueryGamesAsync(filter, Page, PageItemSize);
        var countTask = _mongo.CountAsync(filter);
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
    private async Task LoadGroupedDataAsync(BsonDocument? filter)
    {
        var allGames = await _mongo.QueryAllGamesAsync(filter);
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
    private async Task LoadAggregationsAsync(BsonDocument? filter)
    {
        var emotionTask = _mongo.GroupByEmotionAsync(filter);
        var locationTask = _mongo.GroupByLocationAsync(filter);
        var playStateTask = _mongo.GroupByPlayStateAsync(filter);
        var starTask = _mongo.GroupByStarAsync(filter);
        var cvTask = _mongo.GroupByCVAsync(filter);
        var trueCVTask = _mongo.GroupByTrueCVAsync(filter);
        var tagTask = _mongo.GroupByTagAsync(filter);
        var dateTask = _mongo.GroupByDateAsync(filter);
        var brandTask = _mongo.GroupByBrandAsync(filter);

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

    /// <summary>情感值 → 显示标签</summary>
    private static string GetEmotionLabel(int e) => e switch
    {
        (int)Emotion.HATE => "嫌い",
        (int)Emotion.NORMAL => "...",
        (int)Emotion.HOPE => "気になり",
        (int)Emotion.LIKE => "好き",
        _ => e.ToString()
    };

    /// <summary>游玩状态值 → 显示标签</summary>
    private static string GetPlayStateLabel(int s) => s switch
    {
        (int)PlayState.NOT_PLAY => "...",
        (int)PlayState.PLAYING => "進行中",
        (int)PlayState.PLAYED => "プレイ済み",
        _ => s.ToString()
    };

    /// <summary>
    /// 构建 MongoDB 查询条件 BsonDocument
    /// 将 UI 中的搜索条件（包含/排除 + 日期范围）转为 MongoDB 查询语法
    /// 对应前端的 getAdvSearchJson 逻辑
    /// </summary>
    private BsonDocument? BuildFilter()
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

        var andList = new BsonArray();

        // 遍历搜索条件，构建对应的 MongoDB 查询表达式
        foreach (var opt in allOpts)
        {
            var isExclude = opt.Filter == FilterType.Exclude;
            var value = opt.Value.Trim();

            switch (opt.Key)
            {
                case SearchType.REGEX:
                {
                    // name 字段正则匹配
                    var raw = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("name", isExclude ? new BsonDocument("$not", raw) : raw));
                    break;
                }
                case SearchType.TEXT:
                {
                    // 全文检索（MongoDB $text 索引）
                    var raw = new BsonDocument("$search", value);
                    andList.Add(new BsonDocument("$text", isExclude ? new BsonDocument("$not", raw) : raw));
                    break;
                }
                case SearchType.TAG:
                {
                    // tag 数组正则匹配
                    var raw = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("tag", isExclude ? new BsonDocument("$not", raw) : raw));
                    break;
                }
                case SearchType.STAR:
                {
                    if (int.TryParse(value, out var starValue))
                        andList.Add(new BsonDocument("star", isExclude ? new BsonDocument("$ne", starValue) : starValue));
                    break;
                }
                case SearchType.EMOTION:
                {
                    if (int.TryParse(value, out var emotionVal))
                        andList.Add(new BsonDocument("emotion", isExclude ? new BsonDocument("$ne", emotionVal) : emotionVal));
                    break;
                }
                case SearchType.LOCATION:
                {
                    if (int.TryParse(value, out var locationVal))
                        andList.Add(new BsonDocument("location", isExclude ? new BsonDocument("$ne", locationVal) : locationVal));
                    break;
                }
                case SearchType.PLAYSTATE:
                {
                    if (int.TryParse(value, out var playStateVal))
                        andList.Add(new BsonDocument("playState", isExclude ? new BsonDocument("$ne", playStateVal) : playStateVal));
                    break;
                }
                case SearchType.PAINTER:
                    andList.Add(new BsonDocument("painter", isExclude ? new BsonDocument("$ne", value) : value));
                    break;
                case SearchType.WRITER:
                    andList.Add(new BsonDocument("writer", isExclude ? new BsonDocument("$ne", value) : value));
                    break;
                case SearchType.CV:
                {
                    var cvVal = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("gamechar.cv", isExclude ? new BsonDocument("$not", cvVal) : cvVal));
                    break;
                }
                case SearchType.TRUECV:
                {
                    var truecvVal = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("gamechar.truecv", isExclude ? new BsonDocument("$not", truecvVal) : truecvVal));
                    break;
                }
                case SearchType.BRAND:
                {
                    var brandVal = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("brand.name", isExclude ? new BsonDocument("$not", brandVal) : brandVal));
                    break;
                }
            }
        }

        // 添加日期范围查询条件（半开区间 [start, end)）
        if (hasDateRange)
        {
            var dateQuery = new BsonDocument();

            if (!string.IsNullOrEmpty(CurrentDateRange.StartMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(CurrentDateRange.StartMonth);
                var startLocal = new DateTime(year, month, 1, 0, 0, 0, DateTimeKind.Local);
                dateQuery["$gte"] = new BsonDateTime(startLocal.ToUniversalTime());
            }

            if (!string.IsNullOrEmpty(CurrentDateRange.EndMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(CurrentDateRange.EndMonth);
                // endDate 为下个月1日（半开区间 [start, end)）
                var endLocal = new DateTime(year, month, 1, 0, 0, 0, DateTimeKind.Local).AddMonths(1);
                dateQuery["$lt"] = new BsonDateTime(endLocal.ToUniversalTime());
            }

            andList.Add(new BsonDocument("publishDate", dateQuery));
        }

        if (andList.Count == 0)
            return null;

        return new BsonDocument("$and", andList);
    }
}
