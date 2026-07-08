using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Navigation;
using ErogeHub.Wpf.Models;
using HandyControl.Data;

namespace ErogeHub.Wpf.Controls;

/// <summary>
/// 游戏卡片用户控件 - 可独立在设计器中预览
/// 将 SearchWindow 中的 GameCardTemplate 抽取为独立的 UserControl
/// </summary>
public partial class GameCardControl : UserControl
{
    public GameCardControl()
    {
        InitializeComponent();
    }

    // ==================== Dependency Properties ====================

    /// <summary>游戏数据对象</summary>
    public static readonly DependencyProperty GameProperty =
        DependencyProperty.Register(nameof(Game), typeof(Game), typeof(GameCardControl),
            new PropertyMetadata(null));

    public Game? Game
    {
        get => (Game?)GetValue(GameProperty);
        set => SetValue(GameProperty, value);
    }

    /// <summary>打开详情命令</summary>
    public static readonly DependencyProperty OpenDetailCommandProperty =
        DependencyProperty.Register(nameof(OpenDetailCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? OpenDetailCommand
    {
        get => (ICommand?)GetValue(OpenDetailCommandProperty);
        set => SetValue(OpenDetailCommandProperty, value);
    }

    /// <summary>循环切换卡片情感命令</summary>
    public static readonly DependencyProperty CycleCardEmotionCommandProperty =
        DependencyProperty.Register(nameof(CycleCardEmotionCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? CycleCardEmotionCommand
    {
        get => (ICommand?)GetValue(CycleCardEmotionCommandProperty);
        set => SetValue(CycleCardEmotionCommandProperty, value);
    }

    /// <summary>循环切换游玩状态命令</summary>
    public static readonly DependencyProperty CycleCardPlayStateCommandProperty =
        DependencyProperty.Register(nameof(CycleCardPlayStateCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? CycleCardPlayStateCommand
    {
        get => (ICommand?)GetValue(CycleCardPlayStateCommandProperty);
        set => SetValue(CycleCardPlayStateCommandProperty, value);
    }

    /// <summary>删除卡片命令</summary>
    public static readonly DependencyProperty DeleteCardCommandProperty =
        DependencyProperty.Register(nameof(DeleteCardCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? DeleteCardCommand
    {
        get => (ICommand?)GetValue(DeleteCardCommandProperty);
        set => SetValue(DeleteCardCommandProperty, value);
    }

    /// <summary>品牌点击搜索命令</summary>
    public static readonly DependencyProperty BrandClickToSearchCommandProperty =
        DependencyProperty.Register(nameof(BrandClickToSearchCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? BrandClickToSearchCommand
    {
        get => (ICommand?)GetValue(BrandClickToSearchCommandProperty);
        set => SetValue(BrandClickToSearchCommandProperty, value);
    }

    /// <summary>切换卡片存储位置命令</summary>
    public static readonly DependencyProperty ToggleCardLocationCommandProperty =
        DependencyProperty.Register(nameof(ToggleCardLocationCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? ToggleCardLocationCommand
    {
        get => (ICommand?)GetValue(ToggleCardLocationCommandProperty);
        set => SetValue(ToggleCardLocationCommandProperty, value);
    }

    /// <summary>标签点击搜索命令</summary>
    public static readonly DependencyProperty TagClickToSearchCommandProperty =
        DependencyProperty.Register(nameof(TagClickToSearchCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? TagClickToSearchCommand
    {
        get => (ICommand?)GetValue(TagClickToSearchCommandProperty);
        set => SetValue(TagClickToSearchCommandProperty, value);
    }

    /// <summary>星级变更命令（参数：(int GameId, int Star)）</summary>
    public static readonly DependencyProperty StarChangedCommandProperty =
        DependencyProperty.Register(nameof(StarChangedCommand), typeof(ICommand), typeof(GameCardControl));

    public ICommand? StarChangedCommand
    {
        get => (ICommand?)GetValue(StarChangedCommandProperty);
        set => SetValue(StarChangedCommandProperty, value);
    }

    // ==================== 事件处理器 ====================

    /// <summary>星级评分变更事件</summary>
    private void CardRate_ValueChanged(object sender, FunctionEventArgs<double> e)
    {
        if (Game is null || StarChangedCommand is null) return;
        if (StarChangedCommand.CanExecute(null))
            StarChangedCommand.Execute((Game.Id, (int)e.Info));
    }

    /// <summary>超链接导航（用默认浏览器打开 URL）</summary>
    private void OpenHyperlink_RequestNavigate(object sender, RequestNavigateEventArgs e)
    {
        try
        {
            System.Diagnostics.Process.Start(
                new System.Diagnostics.ProcessStartInfo(e.Uri.ToString()) { UseShellExecute = true });
        }
        catch { }
        e.Handled = true;
    }
}
