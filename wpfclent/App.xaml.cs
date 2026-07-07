using System.IO;
using System.Windows;
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

        // 读取数据库连接信息和图片服务器地址
        var mongoConn = config["MongoConnection"]!;
        var dbName = config["DatabaseName"]!;
        var imgServer = config["ImgServer"]!;

        // 注册 DI 服务
        var services = new ServiceCollection();
        services.AddSingleton(new MongoService(mongoConn, dbName, imgServer)); // MongoDB 数据服务（单例）
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
