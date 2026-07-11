using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 聚合数据状态与聚合面板点击命令
/// </summary>
public partial class SearchViewModel
{
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
}
