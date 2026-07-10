using System.Linq.Expressions;
using ErogeHub.Wpf.Models;
using MongoDB.Bson;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 搜索条件过滤器构建器：把 UI 中的搜索条件（包含/排除 + 日期范围）转为 MongoDB 强类型 FilterDefinition。
/// 从 SearchViewModel 抽离的纯逻辑，无状态、可静态调用。
/// </summary>
public static class SearchFilterBuilder
{
    /// <summary>
    /// 构建 MongoDB 强类型查询条件 FilterDefinition&lt;Game&gt;。
    /// 无任何条件时返回 null（查询全部）。
    /// </summary>
    /// <param name="includeOpts">包含条件列表</param>
    /// <param name="excludeOpts">排除条件列表</param>
    /// <param name="isDateRangeEnabled">是否启用日期范围筛选</param>
    /// <param name="currentDateRange">当前日期范围</param>
    public static FilterDefinition<Game>? Build(
        IEnumerable<SearchOpt> includeOpts,
        IEnumerable<SearchOpt> excludeOpts,
        bool isDateRangeEnabled,
        DateRange currentDateRange)
    {
        // 收集所有已启用且有值的搜索条件
        var allOpts = includeOpts.Concat(excludeOpts)
            .Where(o => o.Enabled && !string.IsNullOrWhiteSpace(o.Value))
            .ToList();

        var hasDateRange = isDateRangeEnabled &&
            (!string.IsNullOrEmpty(currentDateRange.StartMonth) || !string.IsNullOrEmpty(currentDateRange.EndMonth));

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

            if (!string.IsNullOrEmpty(currentDateRange.StartMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(currentDateRange.StartMonth);
                var startLocal = new DateTime(year, month, 1, 0, 0, 0, DateTimeKind.Local);
                dateFilters.Add(fb.Gte(g => g.PublishDate, startLocal.ToUniversalTime()));
            }

            if (!string.IsNullOrEmpty(currentDateRange.EndMonth))
            {
                var (year, month) = DateRangeHelper.SplitMonth(currentDateRange.EndMonth);
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
