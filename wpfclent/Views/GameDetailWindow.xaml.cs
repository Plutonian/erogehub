using System.Windows;
using System.Windows.Controls.Primitives;
using System.Windows.Navigation;
using ErogeHub.Wpf.ViewModels;
using HandyControl.Data;

namespace ErogeHub.Wpf.Views;

public partial class GameDetailWindow : Window
{
    public GameDetailWindow()
    {
        InitializeComponent();
    }

    private void DetailRate_ValueChanged(object sender, FunctionEventArgs<double> e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            _ = vm.UpdateStarAsync((int)e.Info);
        }
    }

    private void DetailMarkSame_Click(object sender, RoutedEventArgs e)
    {
        if (sender is ToggleButton tb && DataContext is GameDetailViewModel vm)
        {
            _ = vm.ToggleMarkSameAsync(tb.IsChecked == true);
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

    private void CancelEdit_Click(object sender, RoutedEventArgs e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            vm.IsEditModalVisible = false;
        }
    }
}
