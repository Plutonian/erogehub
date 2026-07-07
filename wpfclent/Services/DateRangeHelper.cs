namespace ErogeHub.Wpf.Services;

/// <summary>
/// 日期范围，格式 "yyyy-MM"，空字符串表示未设置
/// </summary>
public record DateRange(string StartMonth, string EndMonth);

public record DatePreset(string Label, string Key);

public static class DateRangeHelper
{
    /// <summary>
    /// 日期范围预设列表（扁平，供 ComboBox 绑定）
    /// </summary>
    public static readonly List<DatePreset> Presets = new()
    {
        new("这个月", "THIS_MONTH"),
        new("上一个月", "LAST_MONTH"),
        new("下个月", "NEXT_MONTH"),
        new("过去3个月", "RECENT_3_MONTHS"),
        new("过去半年", "RECENT_HALF_YEAR"),
        new("未来3个月", "NEXT_3_MONTHS"),
        new("未来半年", "NEXT_HALF_YEAR"),
        new("今年", "THIS_YEAR"),
        new("去年", "LAST_YEAR"),
        new("前年", "YEAR_BEFORE_LAST"),
        new("明年", "NEXT_YEAR"),
        new("过去3年", "RECENT_3_YEARS"),
        new("过去5年", "RECENT_5_YEARS"),
        new("过去10年", "RECENT_10_YEARS"),
        new("自定义", "CUSTOM"),
    };

    /// <summary>
    /// 根据预设 key 计算日期范围
    /// </summary>
    public static DateRange GetDateRange(string presetKey)
    {
        var now = DateTime.Now;
        return presetKey switch
        {
            "CUSTOM" => new DateRange("", ""),
            "RECENT_10_YEARS" => new(FormatMonth(new DateTime(now.Year - 9, 1, 1)), FormatMonth(now)),
            "RECENT_5_YEARS" => new(FormatMonth(new DateTime(now.Year - 4, 1, 1)), FormatMonth(now)),
            "RECENT_3_YEARS" => new(FormatMonth(new DateTime(now.Year - 2, 1, 1)), FormatMonth(now)),
            "LAST_YEAR" => new(FormatMonth(new DateTime(now.Year - 1, 1, 1)), FormatMonth(new DateTime(now.Year, 1, 1))),
            "YEAR_BEFORE_LAST" => new(FormatMonth(new DateTime(now.Year - 2, 1, 1)), FormatMonth(new DateTime(now.Year - 1, 1, 1))),
            "THIS_YEAR" => new(FormatMonth(new DateTime(now.Year, 1, 1)), FormatMonth(new DateTime(now.Year + 1, 1, 1))),
            "NEXT_YEAR" => new(FormatMonth(new DateTime(now.Year + 1, 1, 1)), FormatMonth(new DateTime(now.Year + 2, 1, 1))),
            "RECENT_HALF_YEAR" => new(FormatMonth(now.AddMonths(-5)), FormatMonth(now)),
            "RECENT_3_MONTHS" => new(FormatMonth(now.AddMonths(-2)), FormatMonth(now)),
            "THIS_MONTH" => new(FormatMonth(new DateTime(now.Year, now.Month, 1)), FormatMonth(new DateTime(now.Year, now.Month, 1))),
            "LAST_MONTH" => new(FormatMonth(now.AddMonths(-1)), FormatMonth(now.AddMonths(-1))),
            "NEXT_MONTH" => new(FormatMonth(now.AddMonths(1)), FormatMonth(now.AddMonths(1))),
            "NEXT_3_MONTHS" => new(FormatMonth(now.AddMonths(1)), FormatMonth(now.AddMonths(3))),
            "NEXT_HALF_YEAR" => new(FormatMonth(now.AddMonths(1)), FormatMonth(now.AddMonths(6))),
            _ => new DateRange("", ""),
        };
    }

    /// <summary>
    /// 调整日期范围（+/- 月或年），切换到自定义模式
    /// </summary>
    public static DateRange AdjustDateRange(DateRange current, int delta, bool isYear)
    {
        var start = ParseMonth(current.StartMonth);
        var end = ParseMonth(current.EndMonth);

        if (isYear)
        {
            start = start.AddYears(delta);
            end = end.AddYears(delta);
        }
        else
        {
            start = start.AddMonths(delta);
            end = end.AddMonths(delta);
        }

        return new DateRange(FormatMonth(start), FormatMonth(end));
    }

    /// <summary>
    /// 解析 "yyyy-MM" 为 DateTime（该月1日）
    /// </summary>
    public static DateTime ParseMonth(string s)
    {
        if (string.IsNullOrEmpty(s)) return new DateTime(DateTime.Now.Year, DateTime.Now.Month, 1);
        var parts = s.Split('-');
        return new DateTime(int.Parse(parts[0]), int.Parse(parts[1]), 1);
    }

    /// <summary>
    /// 格式化 DateTime 为 "yyyy-MM"
    /// </summary>
    public static string FormatMonth(DateTime dt) => $"{dt:yyyy-MM}";

    /// <summary>
    /// 将 "yyyy-MM" 转为 (year, month) 元组，month 为 1-based
    /// </summary>
    public static (int year, int month) SplitMonth(string s)
    {
        var parts = s.Split('-');
        return (int.Parse(parts[0]), int.Parse(parts[1]));
    }
}
