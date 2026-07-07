using System.Globalization;
using System.Windows;
using System.Windows.Data;
using System.Windows.Media;
using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.Converters;

public class BoolToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is true ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is Visibility.Visible;
}

public class InverseBoolConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !(value is true);

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !(value is true);
}

/// <summary>
/// 游玩状态 → 边条颜色（NOT_PLAY 红 / PLAYING 绿 / PLAYED 蓝）
/// </summary>
public class PlayStateToBrushConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        return value switch
        {
            (int)PlayState.NOT_PLAY => new SolidColorBrush(Color.FromRgb(0xFF, 0x44, 0x44)),
            (int)PlayState.PLAYING => new SolidColorBrush(Color.FromRgb(0x44, 0xAA, 0x44)),
            (int)PlayState.PLAYED => new SolidColorBrush(Color.FromRgb(0x44, 0x88, 0xFF)),
            _ => Brushes.Transparent,
        };
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 情感 → 卡片边框颜色（HATE 时暗红）
/// </summary>
public class EmotionToBrushConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        return value is (int)Emotion.HATE
            ? new SolidColorBrush(Color.FromRgb(0x8B, 0x00, 0x00))
            : Brushes.Transparent;
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// SearchType == STAR → Visible
/// </summary>
public class IsStarInputConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is SearchType.STAR ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// SearchType 为 EMOTION/LOCATION/PLAYSTATE → Visible
/// </summary>
public class IsSelectInputConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is SearchType.EMOTION or SearchType.LOCATION or SearchType.PLAYSTATE
            ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 非星级非下拉 → Visible（文本输入）
/// </summary>
public class IsTextInputConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is SearchType.STAR or SearchType.EMOTION or SearchType.LOCATION or SearchType.PLAYSTATE
            ? Visibility.Collapsed : Visibility.Visible;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 非空字符串 → Visible
/// </summary>
public class StringToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !string.IsNullOrWhiteSpace(value as string) ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// Brand 有 website → Visible
/// </summary>
public class WebsiteToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !string.IsNullOrWhiteSpace(value as string) ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 字符串 ↔ double 双向转换（供 hc:Rate 绑定 string 类型的 Value）
/// </summary>
public class StringDoubleConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => double.TryParse(value as string, out var d) ? d : 0;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value?.ToString() ?? "0";
}

/// <summary>
/// 集合为空 → Visible（用于空状态提示）
/// </summary>
public class EmptyToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is System.Collections.ICollection c) return c.Count == 0 ? Visibility.Visible : Visibility.Collapsed;
        if (value is int i) return i == 0 ? Visibility.Visible : Visibility.Collapsed;
        return Visibility.Collapsed;
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 星级 → 字符串显示（0 显示空）
/// </summary>
public class StarToTextConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is int star && star > 0 ? $"{star}★" : "";

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// DateTime → 相对日期格式（今日/昨日/明日/あとN日/N日前/YY-MM-DD/YYYY-MM）
/// 对应 Angular DateFormatPipe 逻辑
/// </summary>
public class DateRelativeConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is not DateTime date || date.Year == 0) return "";

        var today = DateTime.Today;
        var inputDate = date.Date;
        var daysDiff = (inputDate - today).Days;

        if (daysDiff == 0) return "今日";
        if (daysDiff == -1) return "昨日";
        if (daysDiff == 1) return "明日";
        if (daysDiff > 0) return $"あと{daysDiff}日";
        if (daysDiff > -8) return $"{-daysDiff}日前";

        return inputDate.ToString("yyyy-MM");
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 集合/数量非空 → Visible（count > 0 时显示）
/// </summary>
public class NotEmptyToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is System.Collections.ICollection c) return c.Count > 0 ? Visibility.Visible : Visibility.Collapsed;
        if (value is int i) return i > 0 ? Visibility.Visible : Visibility.Collapsed;
        return Visibility.Collapsed;
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}
