using ErogeHub.Wpf.Models;
using MongoDB.Driver;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 声优（CV）数据访问：加载 cv 集合到内存字典并缓存。
/// 构造时后台预热，避免首次查询游戏时才加载导致首屏卡顿。
/// </summary>
public class CvRepository
{
    private readonly MongoContext _context;

    /// <summary>声优评分缓存（按 trueCV 名匹配），首次访问后驻留内存</summary>
    private Dictionary<string, CV>? _cvMap;

    public CvRepository(MongoContext context)
    {
        _context = context;
        // 后台预热 CV 字典，避免首次查询游戏时才加载导致首屏卡顿
        _ = Task.Run(InitializeCvMapAsync);
    }

    /// <summary>后台预加载 CV 字典（失败不抛异常，首次用到时会再尝试）</summary>
    private async Task InitializeCvMapAsync()
    {
        try { await GetCvMapAsync(); }
        catch { /* 预热失败不影响主流程，后续用到时会重试 */ }
    }

    /// <summary>从 cv 集合加载所有声优数据到内存字典（强类型查询，首次访问后缓存）</summary>
    public async Task<Dictionary<string, CV>> GetCvMapAsync()
    {
        if (_cvMap != null) return _cvMap;
        var list = await _context.CVs.Find(Builders<CV>.Filter.Empty).ToListAsync();
        _cvMap = list
            .Where(cv => !string.IsNullOrEmpty(cv.Name))
            .ToDictionary(cv => cv.Name!, cv => cv);
        return _cvMap;
    }
}
