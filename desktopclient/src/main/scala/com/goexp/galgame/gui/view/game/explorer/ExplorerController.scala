package com.goexp.galgame.gui.view.game.explorer

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.VelocityTemplateConfig
import com.goexp.galgame.gui.view.game.explorer.sidebar.{BrandGroupView, DateGroupController, FilterCondition, FilterPanel}
import com.goexp.galgame.gui.view.game.explorer.tableview.TableListController
import com.goexp.galgame.gui.{Config, HGameApp}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.BorderPane
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import org.apache.velocity.VelocityContext
import org.controlsfx.control.PopOver
import scalafx.Includes._

import java.util
import java.util.function.Predicate
import scala.jdk.CollectionConverters._


class ExplorerController extends DefaultController {


  final private val groupCVServ = TaskService(new ByCV(filteredGames))
  final private val groupTagServ = TaskService(new ByTag(filteredGames))
  final private val brandGroupView = new BrandGroupView()
  final private val popPanel = new PopOver
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML var loadingBar: ProgressBar = _

  @FXML private var filterPanel: BorderPane = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  @FXML private var lbItemFullCount: Hyperlink = _
  /**
   * main panel
   */
  @FXML private var tablelist: TableView[Game] = _

  /**
   * Sidebar
   */
  @FXML private var brandGroup: TitledPane = _

  @FXML private var listView: WebView = _
  @FXML private var gridWebView: WebView = _
  @FXML private var detailWebView: WebView = _


  @FXML private var tagWebView: WebView = _
  @FXML private var cvWebView: WebView = _

  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _
  private var filterPredicate: Predicate[Game] = _

  final private val filterCondition = new FilterCondition()
  final private val filter = new FilterPanel()


  def load(games: ObservableList[Game], initPredicate: Predicate[Game] = null): Unit = {


    filterPanel.setVisible(true)

    groupPredicate = null
    filteredGames = new FilteredList(games)


    lbItemCount.text <== Bindings.size(filteredGames).asString()
    lbItemFullCount.text <== Bindings.size(games).asString()

    // set defaultPredicate
    filterPredicate = FilterCondition.mergeDefaultPredicate(initPredicate)

    // set filter
    filteredGames.setPredicate(filterPredicate)
    recount()


    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparatorProperty.bind(tablelist.comparatorProperty)

    loadItems(sortedData)
    loadGroupPanelData(filteredGames)
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

    def openTag(tag: String) = {

      HGameApp.openTag(tag)
    }

    def openCV(cv: String) = {
      HGameApp.loadCVTab(cv, false)
    }
  }

  def recount(): Unit = {
    tablelist.scrollTo(0)


    def reList() = {

      val root = new VelocityContext()

      root.put("IMG_REMOTE", Config.IMG_REMOTE)
      root.put("GetchuGameLocal", GetchuGameLocal)
      root.put("LOCAL", GameLocation.LOCAL)
      root.put("DateUtil", DateUtil)
      root.put("gamelist", filteredGames)

      val str = VelocityTemplateConfig
        .tpl("/tpl/game/explorer/list.html")
        .process(root)

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

      val root = new VelocityContext()

      root.put("IMG_REMOTE", Config.IMG_REMOTE)
      root.put("GetchuGameLocal", GetchuGameLocal)
      root.put("LOCAL", GameLocation.LOCAL)
      root.put("DateUtil", DateUtil)
      root.put("gamelist", filteredGames)

      val str = VelocityTemplateConfig
        .tpl("/tpl/game/explorer/detail_list.html")
        .process(root)


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

      val root = new VelocityContext()

      root.put("IMG_REMOTE", Config.IMG_REMOTE)
      root.put("GetchuGameLocal", GetchuGameLocal)
      root.put("LOCAL", GameLocation.LOCAL)
      root.put("DateUtil", DateUtil)
      root.put("gamelist", filteredGames)

      val str = VelocityTemplateConfig
        .tpl("/tpl/game/explorer/grid.html")
        .process(root)


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

  }

  private def loadGroupPanelData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    brandGroupView.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)
  }

  override protected def initComponent() = {
    popPanel.setAutoHide(true)
    popPanel.setAnimated(false)

    brandGroup.setContent(brandGroupView)
    filterPanel.setCenter(filter)
  }

  override protected def eventBinding(): Unit = {
    initFilterPanel()
    initGroupPanel()
  }

  private def initGroupPanel() = {

    dateGroupController.onSetProperty.onChange((_, _, newValue) => {
      if (newValue) {
        filterCondition.date = dateGroupController.selectedDate
        filterCondition.makeDatePredicate()


        groupPredicate = filterCondition.groupPredicate

        filteredGames.setPredicate(FilterCondition.mergePredicate(filterPredicate, groupPredicate))

        recount()
      }

    })


    brandGroupView.selectedBrand.onChange((_, _, newValue) => {
      if (newValue != null) {
        filterCondition.brand = newValue.getValue
        filterCondition.makeBrandPredicate()

        groupPredicate = filterCondition.groupPredicate

        filteredGames.setPredicate(FilterCondition.mergePredicate(filterPredicate, groupPredicate))
        recount()
      }

    })

    groupCVServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {

        val root = new VelocityContext()
        root.put("cvlist", newValue)

        val str = VelocityTemplateConfig
          .tpl("/tpl/game/explorer/sidebar/cvlist.html")
          .process(root)


        // set js obj
        val webEngine = cvWebView.getEngine
        //        webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
        //          if (newState eq Worker.State.SUCCEEDED) {
        //            val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
        //            win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
        //          }
        //        })
        webEngine.loadContent(str)

      }
    })
    groupTagServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {

        val root = new VelocityContext()
        root.put("taglist", newValue)

        val str = VelocityTemplateConfig
          .tpl("/tpl/game/explorer/sidebar/taglist.html")
          .process(root)


        // set js obj
        val webEngine = tagWebView.getEngine
        webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
          if (newState eq Worker.State.SUCCEEDED) {
            val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
            win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
          }
        })
        webEngine.loadContent(str)

      }
    })
  }

  private def initFilterPanel() = {

    // right sideBar
    filter.onSetProperty.addListener((_, _, newV) => {
      if (newV) {
        val load = groupPredicate == null


        filterCondition._selectedGameLocation = filter._selectedGameLocation
        filterCondition._selectedGameState = filter._selectedGameState
        filterCondition._selectedStar = filter._selectedStar
        filterCondition._switchAll = filter._switchAll.get()

        filterCondition.makeFilterPredicate()

        filterPredicate = filterCondition.filterPredicate


        filteredGames.setPredicate(FilterCondition.mergePredicate(filterCondition.filterPredicate, groupPredicate))
        recount()
        if (load)
          loadGroupPanelData(filteredGames)
      }

    })

  }

}