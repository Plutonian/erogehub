namespace ErogeHub.Wpf.Models;

/// <summary>
/// 图片服务配置（静态全局），由 App 启动时初始化。
/// 让 Game 模型能直接计算派生 URL，不依赖数据访问层实例。
/// </summary>
public static class ImageConfig
{
    /// <summary>图片服务器地址（网络模式）</summary>
    public static string ImgServer { get; private set; } = string.Empty;

    /// <summary>是否使用本地图片路径</summary>
    public static bool UseLocalImg { get; private set; }

    /// <summary>本地图片根目录（本地模式）</summary>
    public static string LocalImgPath { get; private set; } = string.Empty;

    /// <summary>App 启动时调用一次，初始化图片配置</summary>
    public static void Initialize(string imgServer, bool useLocalImg, string localImgPath)
    {
        ImgServer = imgServer;
        UseLocalImg = useLocalImg;
        LocalImgPath = localImgPath;
    }

    /// <summary>根据游戏发布年和月计算图片基础路径</summary>
    public static string BuildImgBase(int year, int month, int id)
    {
        return UseLocalImg
            ? $"file:///{LocalImgPath.Replace('\\', '/').TrimEnd('/')}/img/{year}/{month}/{id}/"
            : $"http://{ImgServer}/img/{year}/{month}/{id}/";
    }
}
