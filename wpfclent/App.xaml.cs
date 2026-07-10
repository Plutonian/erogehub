using System.IO;
using System.Windows;
using ErogeHub.Wpf.Models;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.ViewModels;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ErogeHub.Wpf;

/// <summary>
/// 应用程序入口，负责依赖注入容器的构建和主窗口的启动
/// </summary>
public partial class App : Application
{
    /// <summary>依赖注入服务提供者</summary>
    private readonly IServiceProvider _services;

    public App()
    {
        // 从 appsettings.json 加载配置
        var config = new ConfigurationBuilder()
            .SetBasePath(AppContext.BaseDirectory)
            .AddJsonFile("appsettings.json", optional: false)
            .Build();

        // 读取数据库连接信息和图片配置
        var mongoConn = config["MongoConnection"]!;
        var dbName = config["DatabaseName"]!;
        var imgServer = config["ImgServer"]!;
        var useLocalImg = config["ImgMode"]?.Equals("local", StringComparison.OrdinalIgnoreCase) == true;
        var localImgPath = config["LocalImgPath"] ?? "";

        // 初始化图片配置（供 Game 模型计算派生 URL）
        ImageConfig.Initialize(imgServer, useLocalImg, localImgPath);

        // 注册 DI 服务
        var services = new ServiceCollection();
        // MongoDB 数据访问层（按职责拆分，共享 MongoContext）
        services.AddSingleton(new MongoContext(mongoConn, dbName));
        services.AddSingleton<CvRepository>();       // CV 字典加载与缓存
        services.AddSingleton<GameEnricher>();        // Game 派生字段计算
        services.AddSingleton<GameRepository>();      // 查询 + 删除
        services.AddSingleton<GameAggregator>();      // 9 个 GroupBy 聚合
        services.AddSingleton<GameUpdater>();         // 字段更新
        services.AddSingleton<SearchViewModel>();    // 搜索主视图模型（单例）
        services.AddSingleton<SearchWindow>();        // 搜索主窗口（单例）
        services.AddTransient<GameDetailViewModel>(); // 游戏详情视图模型（每次新建）
        services.AddTransient<GameDetailWindow>();     // 游戏详情窗口（每次新建）
        _services = services.BuildServiceProvider();
    }

    /// <summary>
    /// 启动后立即显示搜索主窗口
    /// </summary>
    protected override void OnStartup(StartupEventArgs e)
    {
        base.OnStartup(e);
        var window = _services.GetRequiredService<SearchWindow>();
        window.Show();
    }
}
