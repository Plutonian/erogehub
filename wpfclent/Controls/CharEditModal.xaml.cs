using System.Windows.Controls;

namespace ErogeHub.Wpf.Controls;

/// <summary>
/// 角色编辑弹窗用户控件 - 用于编辑角色 CV 和设置 SetMan
/// DataContext 继承自父级（GameDetailViewModel）
/// </summary>
public partial class CharEditModal : UserControl
{
    public CharEditModal()
    {
        InitializeComponent();
    }
}
