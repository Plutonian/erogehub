using System.Globalization;
using System.Windows;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.Converters;

/// <summary>
/// bool → Visibility 转换器：true=Visible, false=Collapsed
/// </summary>
public class BoolToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is true ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value is Visibility.Visible;
}

/// <summary>
/// bool 取反转换器
/// </summary>
public class InverseBoolConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !(value is true);

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => !(value is true);
}

/// <summary>
/// 游玩状态 → 卡片边框颜色（NOT_PLAY=红色 / PLAYING=绿色 / PLAYED=蓝色）
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
/// 情感值 → 卡片边框颜色（HATE(-99) 时显示暗红边框）
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
/// 空字符串 → Visible（StringToVisibilityConverter 的反向）
/// </summary>
public class InverseStringToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => string.IsNullOrWhiteSpace(value as string) ? Visibility.Visible : Visibility.Collapsed;

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}

/// <summary>
/// 非 null 对象 → Visible
/// </summary>
public class NotNullToVisibilityConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        => value != null ? Visibility.Visible : Visibility.Collapsed;

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
    {
        if (!double.TryParse(value as string, out var d)) return 0;
        return Math.Clamp(d, 0, 5);
    }

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

/// <summary>
/// MultiValueConverter：[0]=图片URL, [1]=Emotion
/// Emotion == HATE(-99) 时返回 Gray8 灰度图（等价于 CSS filter: saturate(0%)）
/// 否则返回正常 BitmapImage
/// </summary>
public class GrayscaleImageConverter : IMultiValueConverter
{
    public object? Convert(object?[] values, Type targetType, object? parameter, CultureInfo culture)
    {
        if (values.Length < 1 || values[0] is not string url || string.IsNullOrEmpty(url))
            return null;

        var bitmap = new BitmapImage();
        bitmap.BeginInit();
        bitmap.UriSource = new Uri(url, UriKind.Absolute);
        bitmap.CacheOption = BitmapCacheOption.OnLoad;
        bitmap.EndInit();

        if (values.Length > 1 && values[1] is int emotion && emotion == (int)Emotion.HATE)
        {
            var gray = new FormatConvertedBitmap();
            gray.BeginInit();
            gray.Source = bitmap;
            gray.DestinationFormat = PixelFormats.Gray8;
            gray.EndInit();
            return gray;
        }

        return bitmap;
    }

    public object?[] ConvertBack(object? value, Type[] targetTypes, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}
