package com.goexp.galgame.gui.view.game.explorer

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings.isNotEmpty
import com.goexp.galgame.common.model.game.{GameImg, GameLocation}
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.{Tpl, Websites}
import com.goexp.galgame.gui.view.game.explorer.sidebar.{BrandGroupView, DateGroupController, FilterPanel}
import com.goexp.galgame.gui.view.game.explorer.tableview.TableListController
import com.goexp.galgame.gui.{Config, HGameApp}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import org.controlsfx.control.PopOver
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.scene.layout.HBox

import java.util
import java.util.function.Predicate
import scala.collection.mutable
import scala.jdk.CollectionConverters._


class ExplorerController extends DefaultController {
  final private val groupCVServ = TaskService(new ByCV(filteredGames))
  final private val groupTagServ = TaskService(new ByTag(filteredGames))
  val view = new BrandGroupView()
  private val popPanel = new PopOver
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML var loadingBar: ProgressBar = _
  @FXML private var filterPanel: TitledPane = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  /**
   * main panel
   */
  @FXML private var tablelist: TableView[Game] = _

  /**
   * Sidebar
   */
  @FXML private var cvList: ListView[DataItem] = _
  @FXML private var tagList: ListView[DataItem] = _
  @FXML private var brandGroup: TitledPane = _

  @FXML private var listView: WebView = _
  @FXML private var gridWebView: WebView = _
  @FXML private var detailWebView: WebView = _
  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _


  val cssTpl = Tpl("css.css", this.getClass)


  val listTpl = Tpl("list-tpl.html", this.getClass)
  val detailTpl = Tpl("detail-tpl.html", this.getClass)
  val starTpl = Tpl("star.html", this.getClass)
  val gridTpl = Tpl("grid-tpl.html", this.getClass)
  val gridContainerTpl = Tpl("grid.html", this.getClass)
  val grid_ContainerTpl = Tpl("grid-container.html", this.getClass)

  val panel = new FilterPanel()

  def load(games: ObservableList[Game], initPredicate: Predicate[Game] = null): Unit = {
    filterPanel.setVisible(true)
    groupPredicate = null
    filteredGames = new FilteredList(games)


    val initP = HGameApp.mergeP(initPredicate)
    // set filter
    filteredGames.setPredicate(initP)
    recount()

    // set defaultPredicate
    panel.predicate = initP

    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparatorProperty.bind(tablelist.comparatorProperty)

    loadItems(sortedData)
    setSideBarData(filteredGames)
  }

  object Page {
    def openDetail(id: Int) = {
      filteredGames.asScala.find { g => g.id == id }.foreach(g => HGameApp.loadDetail(g))
    }

    def openBrand(id: Int) = {
      filteredGames.asScala.find { g => g.id == id }.foreach(g => HGameApp.viewBrand(g.brand))
    }

    def openUrl(url: String) = {
      Websites.open(url)
    }
  }

  def recount(): Unit = {
    tablelist.scrollTo(0)


    def reList() = {
      val htmlPart = filteredGames.asScala.to(LazyList)
        .map { g =>
          val imgUrl = s"${Config.IMG_REMOTE}/game/${GetchuGameLocal.tiny120Img(g)}.jpg"
          val titles = g.getTitles


          val tags = g.tag.asScala.to(LazyList)
            .filter(isNotEmpty)
            .map { tag => s"<tag class='tag'>${tag}</tag>" }
            .foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          val stars = (0 until g.star.get()).to(LazyList).map { _ => starTpl.get() }.foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          listTpl
            .put("titles.mainTitle", titles.mainTitle)
            .put("titles.subTitle", titles.subTitle)
            .put("g.id", g.id.toString)
            .put("imgUrl", imgUrl)
            .put("brand.id", g.brand.id.toString)
            .put("brand.name", g.brand.name)
            .put("brand.website", g.brand.website)
            .put("g.state", g.state.get().name)
            .put("g.location", if (g.location.get() eq GameLocation.LOCAL) "green" else "red")
            .put("stars", stars)

            .put("tags", tags)
            .put("g.publishDate", if (DateUtil.needFormat(g.publishDate)) s"${DateUtil.formatDate(g.publishDate)}(${g.publishDate.toString})" else g.publishDate.toString)
            .get()


        }
        .foldLeft[mutable.StringBuilder](new mutable.StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


      val str = gridContainerTpl
        .put("css", cssTpl.get())
        .put("htmlPart", htmlPart)
        .get()


      // set js obj
      val webEngine = listView.getEngine
      webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
        if (newState eq Worker.State.SUCCEEDED) {
          val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
          win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
        }
      })
      webEngine.loadContent(str)

    }

    reList()


    def reDetail() = {
      val htmlPart = filteredGames.asScala.to(LazyList)
        .map { g =>
          val imgUrl = s"${Config.IMG_REMOTE}/game/${GetchuGameLocal.tiny200Img(g)}.jpg"
          val titles = g.getTitles


          val tags = g.tag.asScala.to(LazyList)
            .filter(isNotEmpty)
            .map { tag => s"<tag class='tag'>${tag}</tag>" }
            .foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()

          val images = g.gameImgs.asScala.to(LazyList).zipWithIndex
            .map { case (img: GameImg, i: Int) =>
              val imgUrl = s"${Config.IMG_REMOTE}/game/${GetchuGameLocal.smallSimpleImg(g, i + 1)}.jpg"
              s"<img class='sample_img' src='${imgUrl}'/>"
            }
            .foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          val stars = (0 until g.star.get()).to(LazyList).map { _ => starTpl.get() }.foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          detailTpl
            .put("titles.mainTitle", titles.mainTitle)
            .put("titles.subTitle", titles.subTitle)
            .put("g.id", g.id.toString)
            .put("imgUrl", imgUrl)
            .put("brand.id", g.brand.id.toString)
            .put("brand.name", g.brand.name)
            .put("brand.website", g.brand.website)
            .put("g.state", g.state.get().name)
            .put("g.location", if (g.location.get() eq GameLocation.LOCAL) "green" else "red")
            .put("stars", stars)
            //            .put("text", g.story)

            .put("tags", tags)
            .put("images", images)
            .put("g.publishDate", if (DateUtil.needFormat(g.publishDate)) s"${DateUtil.formatDate(g.publishDate)}(${g.publishDate.toString})" else g.publishDate.toString)
            .get()


        }
        .foldLeft[mutable.StringBuilder](new mutable.StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


      val str = gridContainerTpl
        .put("css", cssTpl.get())
        .put("htmlPart", htmlPart)
        .get()


      // set js obj
      val webEngine = detailWebView.getEngine
      webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
        if (newState eq Worker.State.SUCCEEDED) {
          val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
          win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
        }
      })
      webEngine.loadContent(str)

    }

    reDetail()

    def reGrid() = {
      val htmlPart = filteredGames.asScala.to(LazyList)
        .map { g =>
          val imgUrl = s"${Config.IMG_REMOTE}/game/${GetchuGameLocal.normalImg(g)}.jpg"
          val titles = g.getTitles


          val tags = g.tag.asScala.to(LazyList)
            .filter(isNotEmpty)
            .map { tag => s"<tag class='tag'>${tag}</tag>" }
            .foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          val stars = (0 until g.star.get()).to(LazyList).map { _ => starTpl.get() }.foldLeft[StringBuilder](new StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


          gridTpl
            .put("titles.mainTitle", titles.mainTitle)
            .put("titles.subTitle", titles.subTitle)
            .put("g.id", g.id.toString)
            .put("imgUrl", imgUrl)
            .put("brand.id", g.brand.id.toString)
            .put("brand.name", g.brand.name)
            .put("brand.website", g.brand.website)
            .put("g.state", g.state.get().name)
            .put("g.location", if (g.location.get() eq GameLocation.LOCAL) "green" else "red")
            .put("stars", stars)


            .put("tags", tags)
            .put("g.publishDate", if (DateUtil.needFormat(g.publishDate)) s"${DateUtil.formatDate(g.publishDate)}(${g.publishDate.toString})" else g.publishDate.toString)
            .get()


        }
        .foldLeft[mutable.StringBuilder](new mutable.StringBuilder()) { case (builder, s) => builder.append(s) }.toString()


      val str = grid_ContainerTpl
        .put("css", cssTpl.get())
        .put("htmlPart", htmlPart)
        .get()


      // set js obj
      val webEngine = gridWebView.getEngine
      webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
        if (newState eq Worker.State.SUCCEEDED) {
          val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
          win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
        }
      })
      webEngine.loadContent(str)

    }

    reGrid()


    resetCount(filteredGames)
  }

  private def resetCount(filteredGames: util.List[Game]) = {
    lbItemCount.setText(s"${filteredGames.size} 件")
  }

  private def setSideBarData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    view.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)
  }

  override protected def initialize() = {

    initSideBar()
    initGroupPanel()

    popPanel.setAutoHide(true)
    popPanel.setAnimated(false)


  }

  private def initGroupPanel() = {
    cvList.setCellFactory(_ =>
      new ListCell[DataItem] {

        import VO._

        object VO {
          val _title = new StringProperty()
          val _count = new StringProperty()
        }

        val c = new HBox {
          children ++= Seq(
            new Label {
              textProperty() <== _title
            },
            new Label {
              textProperty <== _count
            }
          )
        }

        itemProperty().onChange { (_, _, item) =>
          if (item != null) {

            val SampleItem(title, count) = item
            _title.value = title
            _count.value = s"($count)"

            setGraphic(c)
          }
          else {
            //            _title.value = ""
            //            _count.value = ""

            setGraphic(null)
          }

        }
      }
    )

    cvList.getSelectionModel.selectedItemProperty().addListener((_, _, cv) => {
      cv match {
        case SampleItem(title, _) =>
          val defaultP: Predicate[Game] = (g: Game) => Option(g.gameCharacters).exists(_.asScala.exists(_.getShowCV().exists(_ == title)))

          groupPredicate = defaultP
          val filterPredicate = panel.predicate
          val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
          else groupPredicate
          filteredGames.setPredicate(p)
          recount()
        case _ =>
      }

    })

    tagList.setCellFactory(_ =>
      new ListCell[DataItem] {

        import VO._

        object VO {
          val _title = new StringProperty()
          val _count = new StringProperty()
        }

        val c = new HBox {
          children ++= Seq(
            new Label {
              textProperty() <== _title
              getStyleClass.add("tag")
            },
            new Label {
              textProperty <== _count
            }
          )
        }

        itemProperty().onChange { (_, _, item) =>
          if (item != null) {

            val SampleItem(title, count) = item
            _title.value = title
            _count.value = s"($count)"

            setGraphic(c)
          }
          else {
            //            _title.value = ""
            //            _count.value = ""

            setGraphic(null)
          }

        }
      }
    )

    groupCVServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        cvList.getSelectionModel.clearSelection()
        cvList.setItems(FXCollections.observableList(newValue))
      }
    })
    groupTagServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) tagList.setItems(FXCollections.observableList(newValue))
    })
  }

  private def initSideBar() = {

    initSidebarContentView()
  }

  private def initSidebarContentView() = {
    filterPanel.setContent(panel)

    panel.onSetProperty.addListener((_, _, newV) => {
      if (newV) {
        val load = groupPredicate == null
        val filterPredicate = panel.predicate
        val p = if (groupPredicate != null) filterPredicate.and(groupPredicate)
        else filterPredicate
        filteredGames.setPredicate(p)
        recount()
        if (load) setSideBarData(filteredGames)
      }

    })


    dateGroupController.onSetProperty.addListener((_, _, newValue) => {
      if (newValue) {
        groupPredicate = dateGroupController.predicate
        val filterPredicate = panel.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })


    view.onSetProperty.onChange((_, _, newValue) => {
      if (newValue) {
        groupPredicate = view.predicate
        val filterPredicate = panel.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })
    brandGroup.setContent(view)
  }

}