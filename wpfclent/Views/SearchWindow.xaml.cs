using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Navigation;
using ErogeHub.Wpf.ViewModels;
using HandyControl.Data;

namespace ErogeHub.Wpf.Views;

public partial class SearchWindow : Window
{
    private readonly SearchViewModel _viewModel;

    public SearchWindow(SearchViewModel viewModel)
    {
        InitializeComponent();
        _viewModel = viewModel;
        DataContext = _viewModel;
    }

    private void Pagination_PageUpdated(object sender, FunctionEventArgs<int> e)
    {
        if (_viewModel.GoToPageCommand.CanExecute(e.Info))
        {
            _viewModel.GoToPageCommand.Execute(e.Info);
        }
    }

    private void CardRate_ValueChanged(object sender, FunctionEventArgs<double> e)
    {
        if (sender is FrameworkElement fe && fe.DataContext is Models.Game game)
        {
            _ = _viewModel.UpdateCardStarAsync(game.Id, (int)e.Info);
        }
    }

    private void CardMarkSame_Click(object sender, RoutedEventArgs e)
    {
        if (sender is ToggleButton tb && tb.DataContext is Models.Game game)
        {
            _ = _viewModel.UpdateCardMarkSameAsync(game.Id, tb.IsChecked == true);
        }
    }

    private void OpenHyperlink_RequestNavigate(object sender, RequestNavigateEventArgs e)
    {
        try
        {
            System.Diagnostics.Process.Start(new System.Diagnostics.ProcessStartInfo(e.Uri.ToString()) { UseShellExecute = true });
        }
        catch { }
        e.Handled = true;
    }

}

