using System.Windows;
using System.Windows.Controls;
using ErogeHub.Wpf.ViewModels;

namespace ErogeHub.Wpf.Controls;

/// <summary>
/// 编辑标题弹窗用户控件 - 可独立在设计器中预览
/// DataContext 继承自父级（GameDetailViewModel）
/// </summary>
public partial class EditTitleModal : UserControl
{
    public EditTitleModal()
    {
        InitializeComponent();
    }

    /// <summary>
    /// 取消编辑按钮点击，关闭编辑弹窗
    /// </summary>
    private void CancelEdit_Click(object sender, RoutedEventArgs e)
    {
        if (DataContext is GameDetailViewModel vm)
        {
            vm.IsEditModalVisible = false;
        }
    }
}
