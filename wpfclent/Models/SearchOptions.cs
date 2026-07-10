namespace ErogeHub.Wpf.Models;

using ErogeHub.Wpf.Services;

/// <summary>
/// 搜索界面静态选项列表（供 UI ComboBox 绑定）
/// 从 SearchViewModel 抽离，避免 VM 承载过多静态数据。
/// </summary>
public static class SearchOptions
{
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
}
