using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.DependencyInjection;
using MongoDB.Bson;

namespace ErogeHub.Wpf.ViewModels;

public partial class SearchViewModel : ObservableObject
{
    private readonly MongoService _mongo;
    private readonly IServiceProvider _services;

    // ==================== 选项列表（供 UI 绑定） ====================

    public record SearchTypeOption(string Label, SearchType Type);

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

    public record EnumOption(string Label, string Value);

    public static List<EnumOption> EmotionOptions { get; } = new()
    {
        new("好き", ((int)Emotion.LIKE).ToString()),
        new("気になり", ((int)Emotion.HOPE).ToString()),
        new("...", ((int)Emotion.NORMAL).ToString()),
        new("嫌い", ((int)Emotion.HATE).ToString()),
    };

    public static List<EnumOption> LocationOptions { get; } = new()
    {
        new("Local", ((int)Location.LOCAL).ToString()),
        new("Remote", ((int)Location.REMOTE).ToString()),
    };

    public static List<EnumOption> PlayStateOptions { get; } = new()
    {
        new("...", ((int)PlayState.NOT_PLAY).ToString()),
        new("進行中", ((int)PlayState.PLAYING).ToString()),
        new("プレイ済み", ((int)PlayState.PLAYED).ToString()),
    };

    public static List<DatePreset> DatePresets => DateRangeHelper.Presets;

    // ==================== 游戏列表 / 分页 ====================

    [ObservableProperty]
    private ObservableCollection<Game> _games = new();

    [ObservableProperty]
    private int _page = 1;

    [ObservableProperty]
    private int _pageItemSize = 40;

    [ObservableProperty]
    private long _total;

    [ObservableProperty]
    private int _totalPages;

    [ObservableProperty]
    private bool _isLoading;

    // ==================== 搜索条件 ====================

    [ObservableProperty]
    private string _selectedDatePreset = "THIS_MONTH";

    [ObservableProperty]
    private bool _isDateRangeEnabled = true;

    [ObservableProperty]
    private DateRange _currentDateRange = DateRangeHelper.GetDateRange("THIS_MONTH");

    [ObservableProperty]
    private ObservableCollection<SearchOpt> _includeOpts = new();

    [ObservableProperty]
    private ObservableCollection<SearchOpt> _excludeOpts = new();

    public bool IsCustomDatePreset => SelectedDatePreset == "CUSTOM";

    public string DateRangeDisplay => $"{CurrentDateRange.StartMonth} ~ {CurrentDateRange.EndMonth}";

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

    // ==================== 聚合数据 ====================

    [ObservableProperty]
    private ObservableCollection<EmotionGroupItem> _emotionGroup = new();

    [ObservableProperty]
    private ObservableCollection<StarGroupItem> _starGroup = new();

    [ObservableProperty]
    private ObservableCollection<LocationGroupItem> _locationGroup = new();

    [ObservableProperty]
    private ObservableCollection<PlayStateGroupItem> _playStateGroup = new();

    [ObservableProperty]
    private ObservableCollection<GroupItem> _cvGroup = new();

    [ObservableProperty]
    private ObservableCollection<GroupItem> _trueCVGroup = new();

    [ObservableProperty]
    private ObservableCollection<GroupItem> _tagGroup = new();

    [ObservableProperty]
    private ObservableCollection<DateGroupItem> _dateGroup = new();

    [ObservableProperty]
    private ObservableCollection<BrandGroupItem> _brandGroup = new();

    // ==================== 构造函数 ====================

    public SearchViewModel(MongoService mongo, IServiceProvider services)
    {
        _mongo = mongo;
        _services = services;
        _ = SearchAsync();
    }

    // ==================== 命令 ====================

    [RelayCommand]
    private async Task SearchAsync()
    {
        Page = 1;
        await LoadAllAsync();
    }

    [RelayCommand]
    private async Task GoToPageAsync(int page)
    {
        if (page < 1 || (TotalPages > 0 && page > TotalPages)) return;
        Page = page;
        IsLoading = true;
        try
        {
            var filter = BuildFilter();
            var games = await _mongo.QueryGamesAsync(filter, Page, PageItemSize);
            Games = new ObservableCollection<Game>(games);
        }
        finally
        {
            IsLoading = false;
        }
    }

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

    [RelayCommand]
    private void RemoveSearchOpt(SearchOpt opt)
    {
        if (opt.Filter == FilterType.Include)
            IncludeOpts.Remove(opt);
        else
            ExcludeOpts.Remove(opt);
    }

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

    [RelayCommand]
    private void OnEmotionClicked(EmotionGroupItem item)
        => AddAsIncludeOpt(SearchType.EMOTION, item.Emotion.ToString());

    [RelayCommand]
    private void OnStarClicked(StarGroupItem item)
        => AddAsIncludeOpt(SearchType.STAR, item.Star.ToString());

    [RelayCommand]
    private void OnLocationClicked(LocationGroupItem item)
        => AddAsIncludeOpt(SearchType.LOCATION, item.Location.ToString());

    [RelayCommand]
    private void OnPlayStateClicked(PlayStateGroupItem item)
        => AddAsIncludeOpt(SearchType.PLAYSTATE, item.PlayState.ToString());

    [RelayCommand]
    private void OnCVClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.CV, item.Title);

    [RelayCommand]
    private void OnTrueCVClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.TRUECV, item.Title);

    [RelayCommand]
    private void OnTagClicked(GroupItem item)
        => AddAsIncludeOpt(SearchType.TAG, item.Title);

    [RelayCommand]
    private void OnBrandClicked(BrandGroupItem item)
        => AddAsIncludeOpt(SearchType.BRAND, item.Title);

    [RelayCommand]
    private void TagClickToSearch(string tag)
        => AddAsIncludeOpt(SearchType.TAG, tag);

    [RelayCommand]
    private void BrandClickToSearch(string brand)
        => AddAsIncludeOpt(SearchType.BRAND, brand);

    [RelayCommand]
    private void OnDateClicked(DateGroupItem item)
    {
        SelectedDatePreset = "CUSTOM";
        CurrentDateRange = new(DateRangeHelper.FormatMonth(item.Start), DateRangeHelper.FormatMonth(item.End));
        _ = SearchAsync();
    }

    private void AddAsIncludeOpt(SearchType type, string value)
    {
        IncludeOpts.Add(new SearchOpt(type, value, true, FilterType.Include));
        _ = SearchAsync();
    }

    // ==================== 卡片交互 ====================

    [RelayCommand]
    private void OpenDetail(Game game)
    {
        var vm = _services.GetRequiredService<GameDetailViewModel>();
        vm.SetGame(game);
        var window = _services.GetRequiredService<GameDetailWindow>();
        window.DataContext = vm;
        window.Show();
    }

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

    [RelayCommand]
    private async Task ToggleCardLocationAsync(Game game)
    {
        var next = game.Location == (int)Location.LOCAL ? (int)Location.REMOTE : (int)Location.LOCAL;
        await _mongo.UpdateLocationAsync(game.Id, next);
        game.Location = next;
    }

    [RelayCommand]
    private async Task DeleteCardAsync(Game game)
    {
        await _mongo.DeleteGameAsync(game.Id);
        Games.Remove(game);
        await LoadAllAsync();
    }

    /// <summary>
    /// 卡片星级修改（由 SearchWindow.xaml.cs 的 ValueChanged 事件调用）
    /// </summary>
    public async Task UpdateCardStarAsync(int gameId, int star)
    {
        await _mongo.UpdateStarAsync(gameId, star);
    }

    /// <summary>
    /// 卡片 MarkSame 修改（由 SearchWindow.xaml.cs 的 Click 事件调用）
    /// </summary>
    public async Task UpdateCardMarkSameAsync(int gameId, bool isSame)
    {
        await _mongo.MarkSameAsync(gameId, isSame);
    }

    // ==================== 核心逻辑 ====================

    private async Task LoadAllAsync()
    {
        IsLoading = true;
        try
        {
            var filter = BuildFilter();

            var gamesTask = _mongo.QueryGamesAsync(filter, Page, PageItemSize);
            var countTask = _mongo.CountAsync(filter);
            var emotionTask = _mongo.GroupByEmotionAsync(filter);
            var locationTask = _mongo.GroupByLocationAsync(filter);
            var playStateTask = _mongo.GroupByPlayStateAsync(filter);
            var starTask = _mongo.GroupByStarAsync(filter);
            var cvTask = _mongo.GroupByCVAsync(filter);
            var trueCVTask = _mongo.GroupByTrueCVAsync(filter);
            var tagTask = _mongo.GroupByTagAsync(filter);
            var dateTask = _mongo.GroupByDateAsync(filter);
            var brandTask = _mongo.GroupByBrandAsync(filter);

            await Task.WhenAll(gamesTask, countTask, emotionTask, locationTask, playStateTask,
                starTask, cvTask, trueCVTask, tagTask, dateTask, brandTask);

            Games = new ObservableCollection<Game>(gamesTask.Result);
            Total = countTask.Result;
            TotalPages = Total > 0 ? (int)Math.Ceiling((double)Total / PageItemSize) : 0;
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
        finally
        {
            IsLoading = false;
        }
    }

    /// <summary>
    /// 构建 MongoDB 查询 BsonDocument（从 TypeScript getAdvSearchJson 直译）
    /// </summary>
    private BsonDocument? BuildFilter()
    {
        var allOpts = IncludeOpts.Concat(ExcludeOpts)
            .Where(o => o.Enabled && !string.IsNullOrWhiteSpace(o.Value))
            .ToList();

        var hasDateRange = IsDateRangeEnabled &&
            (!string.IsNullOrEmpty(CurrentDateRange.StartMonth) || !string.IsNullOrEmpty(CurrentDateRange.EndMonth));

        if (allOpts.Count == 0 && !hasDateRange)
            return null;

        var andList = new BsonArray();

        foreach (var opt in allOpts)
        {
            var isExclude = opt.Filter == FilterType.Exclude;
            var value = opt.Value.Trim();

            switch (opt.Key)
            {
                case SearchType.REGEX:
                {
                    var raw = new BsonDocument("$regex", value);
                    andList.Add(new BsonDocument("name", isExclude ? new BsonDocument("$not", raw) : raw));
                    break;
                }
                case SearchType.TEXT:
                {
                    var raw = new BsonDocument("$search", value);
                    andList.Add(new BsonDocument("$text", isExclude ? new BsonDocument("$not", raw) : raw));
                    break;
                }
                case SearchType.TAG:
                {
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

        // 日期范围查询
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
