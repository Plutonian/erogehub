using System.IO;
using System.Windows;
using ErogeHub.Wpf.Services;
using ErogeHub.Wpf.ViewModels;
using ErogeHub.Wpf.Views;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ErogeHub.Wpf;

public partial class App : Application
{
    private readonly IServiceProvider _services;

    public App()
    {
        var config = new ConfigurationBuilder()
            .SetBasePath(AppContext.BaseDirectory)
            .AddJsonFile("appsettings.json", optional: false)
            .Build();

        var mongoConn = config["MongoConnection"]!;
        var dbName = config["DatabaseName"]!;
        var imgServer = config["ImgServer"]!;

        var services = new ServiceCollection();
        services.AddSingleton(new MongoService(mongoConn, dbName, imgServer));
        services.AddSingleton<SearchViewModel>();
        services.AddSingleton<SearchWindow>();
        _services = services.BuildServiceProvider();
    }

    protected override void OnStartup(StartupEventArgs e)
    {
        base.OnStartup(e);
        var window = _services.GetRequiredService<SearchWindow>();
        window.Show();
    }
}
