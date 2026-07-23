# ErogeHub.Wpf

美少女游戏管理客户端，WPF + MongoDB 架构。原项目由 Scala + Angular 实现，前端迁移为纯 WPF。

## 技术栈

- **.NET 10** + **WPF**
- **CommunityToolkit.Mvvm** — MVVM 源码生成器（`[ObservableProperty]`、`[RelayCommand]`）
- **HandyControl** — UI 控件库
- **MongoDB.Driver** — 直接使用驱动，无 EF Core
- **VirtualizingWrapPanel** — 虚拟化瀑布流面板
- **Microsoft.Extensions.DependencyInjection** — 依赖注入

## 项目结构

```
ErogeHub.Wpf/
├─ Models/              数据模型（Game、CV、枚举、聚合项）
├─ Services/            业务服务
│  ├─ Game/             Game 相关（Repository/Aggregator/Enricher/Updater）
│  └─ *.cs              基础设施（MongoContext/CvRepository/SearchFilterBuilder）
├─ ViewModels/          ViewModel（partial class 按功能拆分）
│  ├─ Search/           搜索 VM（5 个 partial 文件）
│  └─ GameDetail/       详情 VM（6 个 partial 文件）
├─ Views/               窗口（SearchWindow、GameDetailWindow）
├─ Controls/            自定义控件（GameCardControl 等）
├─ Styles/              资源字典（Converters/Controls/DataTemplates）
└─ Converters/          值转换器
```

## 性能优化

卡片列表支持 2000+ 条数据流畅滚动，核心优化如下：

### 1. UI 虚拟化 + 容器回收

```xml
<ListBox VirtualizingPanel.IsVirtualizing="True"
         VirtualizingPanel.VirtualizationMode="Recycling"
         VirtualizingPanel.CacheLength="1,1"
         VirtualizingPanel.ScrollUnit="Pixel">
    <VirtualizingWrapPanel/>
</ListBox>
```

- **虚拟化**：只渲染可见区域的 ~10 张卡片，非可见卡片不实例化
- **容器回收（Recycling）**：滚动时复用已回收的 ListBoxItem，不再每次新建/销毁容器
- **预渲染（CacheLength=1,1）**：上下各预渲染 1 屏的卡片，滚动时已提前准备好

### 2. 分层渲染（延迟实例化）

卡片非悬停状态仅 ~8 个控件（封面图 + 情感按钮 + 底部栏），悬停时才实例化覆盖层（~18 个控件）：

```xml
<ContentControl>
    <ContentControl.Style>
        <Style TargetType="ContentControl">
            <Setter Property="Content" Value="{x:Null}"/>
            <Style.Triggers>
                <DataTrigger Binding="{Binding IsMouseOver, ElementName=cardGrid}" Value="True">
                    <Setter Property="Content" Value="{Binding}"/>
                    <Setter Property="ContentTemplate" Value="{StaticResource HoverOverlayTemplate}"/>
                </DataTrigger>
            </Style.Triggers>
        </Style>
    </ContentControl.Style>
</ContentControl>
```

- 非悬停：`Content=null`，覆盖层的 18 个控件**不存在**
- 悬停时：设置 Content + ContentTemplate，WPF 实例化模板内容，淡入动画
- 鼠标离开：Content 清回 null，控件被卸载回收

### 3. 图片预下载 + 解码缓存

**Game 模型缓存 BitmapImage 实例**，虚拟化回收时直接返回缓存，零开销：

```csharp
// Game.cs
[BsonIgnore]
public ImageSource? CoverImage
{
    get => _coverImage ??= CreateCoverImage();
    internal set
    {
        if (_coverImage != value)
        {
            _coverImage = value;
            OnPropertyChanged(nameof(CoverImage));  // 后台设置后通知 UI 刷新
        }
    }
}
```

**GameEnricher 后台 fire-and-forget 预下载**，不阻塞数据加载流程：

```csharp
// GameEnricher.cs — EnrichAsync 只等 CPU 计算完成，图片在后台下载
_ = Task.Run(() =>
{
    Parallel.ForEach(games, ioOpts, game =>
    {
        var bmp = new BitmapImage();
        bmp.BeginInit();
        bmp.DecodePixelWidth = 280;           // 按卡片宽度解码，不解码原图
        bmp.CacheOption = BitmapCacheOption.OnLoad;  // 立即下载+解码
        bmp.UriSource = new Uri(game.CoverUrl);
        bmp.EndInit();
        bmp.Freeze();                         // 冻结，可跨线程访问
        game.CoverImage = bmp;                // PropertyChanged 自动刷新 UI
    });
}, cancellationToken);
```

| 优化点 | 作用 |
|---|---|
| `DecodePixelWidth=280` | 按卡片宽度解码，不解码原始大图（可能 1000px+） |
| `CacheOption.OnLoad` | 在后台线程同步下载+解码，不阻塞 UI 线程 |
| `Freeze()` | 使 BitmapImage 不可变，WPF 渲染更高效 |
| `MaxDegreeOfParallelism=8` | 限制并发避免网络拥塞 |
| Game 模型缓存 | 每个 Game 对象只创建一次 BitmapImage，回收复用时零开销 |
| fire-and-forget | 图片下载不阻塞 `EnrichAsync`，卡片先显示再加载图片 |
| `OnPropertyChanged` | 后台线程设置 `CoverImage` 后通知 UI 刷新，WPF 自动 marshal 到 UI 线程 |

### 4. 绑定路径优化

`RelativeSource AncestorType` 改为 `ElementName`，从可视化树遍历（O(depth)）变为名称字典查找（O(1)）：

```xml
<!-- 改前：遍历可视化树到根 -->
Command="{Binding DataContext.OpenDetailCommand, RelativeSource={RelativeSource AncestorType=Window}}"

<!-- 改后：名称字典直接查找 -->
Command="{Binding DataContext.OpenDetailCommand, ElementName=RootWindow}"
```

共替换 15 处绑定（DataTemplate 8 处 + GameCardControl 内部 7 处）。

### 5. MongoDB 投影 + 批量添加

**列表查询投影**：列表页不需要 `GameCharacters`、`SimpleImgs`、`story`、`intro` 等大字段，投影掉减少网络传输和反序列化开销：

```csharp
// GameRepository.cs
private static readonly ProjectionDefinition<Game, Game> ListProjection =
    new BsonDocumentProjectionDefinition<Game, Game>(new BsonDocument
    {
        { "gamechar", 0 }, { "simpleImg", 0 }, { "story", 0 },
        { "intro", 0 }, { "painter", 0 }, { "writer", 0 }
    });
```

**RangeObservableCollection 批量替换**：避免 2k 次逐个 Add 触发 2k 次 CollectionChanged：

```csharp
// RangeObservableCollection.cs
public void ReplaceAll(IEnumerable<T> items)
{
    Items.Clear();
    foreach (var item in items) Items.Add(item);
    RaiseReset();  // 只触发 1 次 Reset 通知
}
```

### 6. NullSafeDateTimeSerializer

MongoDB 中部分文档的 `publishDate` 为 null，默认 `DateTimeSerializer` 反序列化时抛 `FormatException`。自定义序列化器将 null 安全转为 `DateTime.MinValue`：

```csharp
// Game.cs
public class NullSafeDateTimeSerializer : SerializerBase<DateTime>
{
    private static readonly DateTimeSerializer Inner = new();

    public override DateTime Deserialize(BsonDeserializationContext context, BsonDeserializationArgs args)
    {
        if (context.Reader.GetCurrentBsonType() == BsonType.Null)
        {
            context.Reader.ReadNull();
            return DateTime.MinValue;
        }
        return Inner.Deserialize(context, args);
    }

    public override void Serialize(BsonSerializationContext context, BsonSerializationArgs args, DateTime value)
        => Inner.Serialize(context, args, value);
}
```

## 构建

```bash
dotnet build
dotnet run
```

需要在 `appsettings.json` 中配置 MongoDB 连接字符串和图片服务器地址。
