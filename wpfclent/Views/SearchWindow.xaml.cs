using System.Windows;
using ErogeHub.Wpf.ViewModels;
using HandyControl.Data;

namespace ErogeHub.Wpf.Views;

public partial class SearchWindow : System.Windows.Window
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
}
