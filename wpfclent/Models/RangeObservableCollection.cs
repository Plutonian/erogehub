using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.ComponentModel;

namespace ErogeHub.Wpf.Models;

/// <summary>
/// 支持批量操作的 ObservableCollection，AddRange/ReplaceAll 只触发一次 Reset 通知，
/// 避免 2k 条数据逐个 Add 时的 2k 次 CollectionChanged 开销。
/// </summary>
public class RangeObservableCollection<T> : ObservableCollection<T>
{
    /// <summary>批量添加元素，只触发一次 Reset 通知</summary>
    public void AddRange(IEnumerable<T> items)
    {
        CheckReentrancy();
        foreach (var item in items)
            Items.Add(item);
        RaiseReset();
    }

    /// <summary>清空并批量替换元素，只触发一次 Reset 通知</summary>
    public void ReplaceAll(IEnumerable<T> items)
    {
        CheckReentrancy();
        Items.Clear();
        foreach (var item in items)
            Items.Add(item);
        RaiseReset();
    }

    private void RaiseReset()
    {
        OnPropertyChanged(new PropertyChangedEventArgs(nameof(Count)));
        OnPropertyChanged(new PropertyChangedEventArgs("Item[]"));
        OnCollectionChanged(new NotifyCollectionChangedEventArgs(NotifyCollectionChangedAction.Reset));
    }
}
