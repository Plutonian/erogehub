namespace ErogeHub.Wpf.Models;

public enum Emotion
{
    HATE = -99,
    NORMAL = 0,
    HOPE = 1,
    LIKE = 99
}

public enum Location
{
    REMOTE = 0,
    LOCAL = 1
}

public enum PlayState
{
    NOT_PLAY = 0,
    PLAYING = 1,
    PLAYED = 2
}

public enum SearchType
{
    TEXT,
    REGEX,
    TAG,
    STAR,
    EMOTION,
    LOCATION,
    PLAYSTATE,
    PAINTER,
    WRITER,
    CV,
    TRUECV,
    BRAND
}

public enum FilterType
{
    Include,
    Exclude
}

/// <summary>
/// 日期分组类型
/// </summary>
public enum DateType
{
    YEAR,
    MONTH
}
