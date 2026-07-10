using ErogeHub.Wpf.Models;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 游戏 Enricher：基于已加载的 CV 字典，并行计算 Game 的派生字段
/// （角色头像 URL、CV 关联、截图 URL、PublishDate 时区校正）。
/// </summary>
public class GameEnricher
{
    private readonly CvRepository _cvRepository;

    public GameEnricher(CvRepository cvRepository)
    {
        _cvRepository = cvRepository;
    }

    /// <summary>
    /// 批量并行 enrich：先一次性拿 cvMap（异步），再用 Parallel.ForEach 并行计算每个 Game 的派生字段。
    /// 相比串行 foreach + await，避免了重复 await 调度开销，适合批量场景。
    /// </summary>
    public async Task EnrichAsync(List<Game> games)
    {
        if (games.Count == 0) return;
        var cvMap = await _cvRepository.GetCvMapAsync();
        Parallel.ForEach(games, g => EnrichSync(g, cvMap));
    }

    /// <summary>单条 enrich：用于 QueryGameByIdAsync 等单条查询场景。</summary>
    public async Task EnrichAsync(Game game)
    {
        var cvMap = await _cvRepository.GetCvMapAsync();
        EnrichSync(game, cvMap);
    }

    /// <summary>
    /// 同步版本：在已拿到 cvMap 后，纯 CPU 计算派生字段。供 Parallel.ForEach 并行批量调用。
    /// </summary>
    private static void EnrichSync(Game game, Dictionary<string, CV> cvMap)
    {
        // PublishDate 时区：driver 默认按 UTC 反序列化，转回本地时间保持与原行为一致
        if (game.PublishDate != default && game.PublishDate.Kind == DateTimeKind.Utc)
            game.PublishDate = game.PublishDate.ToLocalTime();

        // 角色头像 URL + CV 关联
        foreach (var c in game.GameCharacters)
        {
            c.AvatarUrl = $"{game.ImgBase}char_s_{c.Index}.jpg";
            if (!string.IsNullOrEmpty(c.TrueCV) && cvMap.TryGetValue(c.TrueCV, out var cv))
                c.CvObj = cv;
        }
        // 截图 URL
        foreach (var img in game.SimpleImgs)
            img.Url = $"{game.ImgBase}simple_l_{img.Index}.jpg";
    }
}
