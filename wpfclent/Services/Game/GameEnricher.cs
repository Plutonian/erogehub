using ErogeHub.Wpf.Models;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace ErogeHub.Wpf.Services;

/// <summary>
/// 游戏 Enricher：基于已加载的 CV 字典，并行计算 Game 的派生字段
/// （角色头像 URL、CV 关联、截图 URL、PublishDate 时区校正、封面图预解码）。
/// </summary>
public class GameEnricher
{
    private readonly CvRepository _cvRepository;

    public GameEnricher(CvRepository cvRepository)
    {
        _cvRepository = cvRepository;
    }

    /// <summary>
    /// 批量并行 enrich：先一次性拿 cvMap（异步），再并行计算派生字段。
    /// 封面图在后台 fire-and-forget 预下载，不阻塞 EnrichAsync 返回。
    /// 图片下载完后通过 PropertyChanged 自动刷新 UI。
    /// </summary>
    public async Task EnrichAsync(List<Game> games, CancellationToken cancellationToken = default)
    {
        if (games.Count == 0) return;
        var cvMap = await _cvRepository.GetCvMapAsync(cancellationToken);

        // 第一步：CPU 计算派生字段（快）
        var cpuOpts = new ParallelOptions { CancellationToken = cancellationToken };
        Parallel.ForEach(games, cpuOpts, g => EnrichSync(g, cvMap));

        // 第二步：后台预下载封面图（不阻塞，下载完后通过 PropertyChanged 自动刷新 UI）
        _ = Task.Run(() =>
        {
            try
            {
                var ioOpts = new ParallelOptions
                {
                    CancellationToken = cancellationToken,
                    MaxDegreeOfParallelism = 8
                };
                Parallel.ForEach(games, ioOpts, PreloadCoverImage);
            }
            catch (OperationCanceledException) { }
        }, cancellationToken);
    }

    /// <summary>单条 enrich：用于 QueryGameByIdAsync 等单条查询场景。</summary>
    public async Task EnrichAsync(Game game, CancellationToken cancellationToken = default)
    {
        var cvMap = await _cvRepository.GetCvMapAsync(cancellationToken);
        EnrichSync(game, cvMap);
        PreloadCoverImage(game);
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

    /// <summary>
    /// 后台线程预下载+解码封面图，Freeze 后缓存到 Game.CoverImage。
    /// 失败时静默忽略，CoverImage getter 会回退到 DelayCreation 在 UI 线程懒加载。
    /// </summary>
    private static void PreloadCoverImage(Game game)
    {
        if (string.IsNullOrEmpty(game.CoverUrl)) return;
        try
        {
            var bmp = new BitmapImage();
            bmp.BeginInit();
            bmp.DecodePixelWidth = 280;
            bmp.UriSource = new Uri(game.CoverUrl);
            bmp.CacheOption = BitmapCacheOption.OnLoad;
            bmp.EndInit();
            bmp.Freeze();
            game.CoverImage = bmp;
        }
        catch
        {
            // 图片加载失败（404/网络错误），CoverImage 保持 null
            // getter 会回退到 CreateCoverImage()（DelayCreation）在渲染时重试
        }
    }
}
