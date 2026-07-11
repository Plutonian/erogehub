using System.Diagnostics;
using CommunityToolkit.Mvvm.Input;

namespace ErogeHub.Wpf.ViewModels;

/// <summary>
/// 外部链接跳转命令
/// </summary>
public partial class GameDetailViewModel
{
    /// <summary>在默认浏览器中打开 URL</summary>
    [RelayCommand]
    private void OpenUrl(string url)
    {
        if (string.IsNullOrEmpty(url)) return;
        try
        {
            Process.Start(new ProcessStartInfo(url) { UseShellExecute = true });
        }
        catch { }
    }

    /// <summary>在外部网站搜索游戏名</summary>
    [RelayCommand]
    private void OpenExternalSearch(string engine)
    {
        if (Game == null) return;
        var title = Uri.EscapeDataString(Game.MainTitle);
        var url = engine switch
        {
            "google" => $"https://www.google.com/search?q={title}",
            "wiki" => $"https://ja.wikipedia.org/w/index.php?search={title}",
            "getchu" => $"http://www.getchu.com/soft.phtml?id={Game.Id}&gc=gc",
            "dlsite" => $"https://www.dlsite.com/pro/fsr/=/language/jp/sex_category[0]/male/keyword/{title}",
            "erogamescape" => $"https://erogamescape.dyndns.org/~ap2/ero/toukei_kaiseki/kensaku.php?category=game&word_category=name&mode=normal&word={title}",
            "2df" => $"https://galge.fun/subjects/search?keyword={title}",
            "ggbases" => $"https://www.ggbases.com/search.so?p=0&advanced=&title={title}",
            _ => "",
        };
        if (!string.IsNullOrEmpty(url))
            OpenUrl(url);
    }
}
