namespace ErogeHub.Wpf.Models;

/// <summary>
/// 情感状态枚举
/// </summary>
public enum Emotion
{
    HATE = -99,   // 嫌い（讨厌）
    NORMAL = 0,   // ...（普通）
    HOPE = 1,     // 気になり（在意）
    LIKE = 99     // 好き（喜欢）
}

/// <summary>
/// 存储位置枚举
/// </summary>
public enum Location
{
    REMOTE = 0,   // 云端
    LOCAL = 1     // 本地
}

/// <summary>
/// 游玩状态枚举
/// </summary>
public enum PlayState
{
    NOT_PLAY = 0,  // 未玩
    PLAYING = 1,   // 正在玩
    PLAYED = 2     // 已通关
}

/// <summary>
/// 搜索类型枚举，对应左侧搜索条件面板的各选项
/// </summary>
public enum SearchType
{
    TEXT,       // 全文检索
    REGEX,      // 正则表达式
    TAG,        // 标签
    STAR,       // 星级
    EMOTION,    // 情感
    LOCATION,   // 位置
    PLAYSTATE,  // 游玩状态
    PAINTER,    // 画师
    WRITER,     // 剧本作家
    CV,         // 声优
    TRUECV,     // 去马甲CV
    BRAND       // 品牌
}

/// <summary>
/// 筛选类型：包含 / 排除
/// </summary>
public enum FilterType
{
    Include,    // 包含（满足条件才显示）
    Exclude     // 排除（满足条件则隐藏）
}

/// <summary>
/// 日期分组粒度
/// </summary>
public enum DateType
{
    YEAR,   // 按年分组
    MONTH   // 按月分组
}

/// <summary>
/// 列表分组类型，用于中间面板对当前查询结果进行内存分组
/// </summary>
public enum GroupType
{
    None,       // 无分组
    Year,       // 按年
    Month,      // 按月
    Star,       // 按星级
    Emotion,    // 按情感
    Location,   // 按位置
    PlayState,  // 按游玩状态
    Brand       // 按品牌
}
