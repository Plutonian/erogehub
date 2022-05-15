package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.search._
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.MainPanelController
import com.goexp.galgame.gui.view.common.control.DataPage
import com.goexp.galgame.gui.view.game.HomeController._
import com.goexp.galgame.gui.view.game.explorer.sidebar.{FilterCondition, FilterPanel}
import com.goexp.galgame.gui.view.game.search.SearchController
import com.goexp.ui.javafx.DefaultController
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import javafx.scene.layout.Pane
import org.controlsfx.control.PopOver
import org.controlsfx.control.PopOver.ArrowLocation
import scalafx.Includes._
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.image.ImageView

import scala.jdk.CollectionConverters._

class ConfigItem[T] {
  var title: String = _
  var icon: ImageView = _
  var dataTask: Task[ObservableList[T]] = _
}

object HomeController {

  var tabPanel: TabPane = _

  private val queryByQuConfig2 = List(
    new ConfigItem[Game] {
      title = "优"
      dataTask = new ByStarRange(4, 5)
    },
    new ConfigItem[Game] {
      title = "良"
      dataTask = new ByStarRange(3, 3)
    },
    new ConfigItem[Game] {
      title = "差"
      dataTask = new ByStarRange(1, 2)
    }
  )

  private val queryByLocationConfig2 = List(
    new ConfigItem[Game] {
      title = GameLocation.LOCAL.name
      dataTask = new ByLocation(GameLocation.LOCAL)
    },
    //    new ConfigItem[Game] {
    //      title = GameLocation.NETDISK.name
    //      dataTask = new ByLocation(GameLocation.NETDISK)
    //    },
    new ConfigItem[Game] {
      title = GameLocation.REMOTE.name
      dataTask = new ByLocation(GameLocation.REMOTE)
    }
  )

  private val queryLocalConfig = List(
    new ConfigItem[Game] {
      title = GameState.PLAYED.name
      dataTask = new ByState(GameState.PLAYED)
    },
    new ConfigItem[Game] {
      title = GameState.PLAYING.name
      dataTask = new ByState(GameState.PLAYING)
    }
  )

  private val queryRemoteConfig = List(
    //    new ConfigItem[Game] {
    //      title = GameState.READYTOVIEW.name
    //      dataTask = new ByState(GameState.READYTOVIEW)
    //    },
    new ConfigItem[Game] {
      title = "Wishlist"
      dataTask = new ByState(GameState.HOPE)
    }
  )

}

class HomeController extends DefaultController {

  @FXML var mainTabPanel: javafx.scene.control.TabPane = _

  @FXML private var gameStateLinkPanel: Pane = _
  @FXML private var gameStateLikeLinkPanel: Pane = _
  @FXML private var queryByQuPanel: Pane = _
  @FXML private var queryByLocationPanel: Pane = _

  @FXML private var linkDate: Hyperlink = _
  @FXML private var linkCV: Hyperlink = _
  @FXML private var linkSearch: Hyperlink = _
  @FXML private var linkTags: Hyperlink = _
  @FXML private var linkBrand: Hyperlink = _

  @FXML private var linkConfig: Hyperlink = _

  private lazy val filterPanel = new FilterPanel()


  override protected def initComponent(): Unit = {

    tabPanel = mainTabPanel

    def configItemToNode(item: ConfigItem[Game]) = {
      new Hyperlink(item.title) {
        setGraphic(item.icon)
        setOnAction { _ =>
          TabManager().open(item.title,
            new DataPage(ExplorerData(item.dataTask)) {
              text = item.title
            }
          )
        }
      }
    }

    def initInLocalList() = {

      val array = queryLocalConfig
        .map { item =>
          configItemToNode(item)
        }

      gameStateLinkPanel.getChildren.setAll(array.asJava)
    }

    def initInRemoteList() = {

      val array = queryRemoteConfig
        .map { item =>
          configItemToNode(item)
        }

      gameStateLikeLinkPanel.getChildren.setAll(array.asJava)
    }

    def initQuList() = {
      val array = queryByQuConfig2
        .map { item =>
          configItemToNode(item)
        }

      queryByQuPanel.getChildren.setAll(array.asJava)
    }

    def initLocationList(): Unit = {
      val array = queryByLocationConfig2
        .map { item =>
          configItemToNode(item)
        }

      queryByLocationPanel.getChildren.setAll(array.asJava)
    }

    initInLocalList()
    initInRemoteList()
    initQuList()
    initLocationList()

    linkDate.setGraphic(new ImageView(LocalRes.IMG_DATE_PNG))

    linkCV.setGraphic(new ImageView(LocalRes.IMG_CV_PNG))

    linkSearch.setGraphic(new ImageView(LocalRes.IMG_search_PNG))

    linkTags.setGraphic(new ImageView(LocalRes.IMG_TAG_PNG))


  }

  override protected def dataBinding(): Unit = super.dataBinding()

  override protected def eventBinding(): Unit = {

    /**
     * Date panel
     */
    val loader = new SimpleFxmlLoader[DateController]("date.fxml")
    val popPanel = new PopOver {
      setArrowLocation(ArrowLocation.LEFT_TOP)
      setAutoHide(true)
      setContentNode(loader.node)
    }
    linkDate.setOnAction { _ =>

      if (!popPanel.isShowing)
        popPanel.show(linkDate)

    }


    /**
     * Filter Panel
     */
    filterPanel.onSetProperty.addListener { (_, _, v) =>
      if (v) {
        val filterCondition = new FilterCondition()

        filterCondition._selectedGameLocation = filterPanel._selectedGameLocation
        filterCondition._selectedGameState = filterPanel._selectedGameState
        filterCondition._selectedStar = filterPanel._selectedStar
        filterCondition._switchAll = filterPanel._switchAll.get()

        filterCondition.makeFilterPredicate()

        FilterCondition.DEFAULT_GAME_PREDICATE = filterCondition.filterPredicate
      }
    }

    val popConfigPanel = new PopOver {
      setArrowLocation(ArrowLocation.BOTTOM_LEFT)
      setAutoHide(true)
      setContentNode(filterPanel)
    }

    linkConfig.setOnAction { _ =>
      if (!popConfigPanel.isShowing)
        popConfigPanel.show(linkConfig)
    }


    /**
     * Other Links
     */

    linkCV.setOnAction { _ =>

      val loader = new SimpleFxmlLoader[CVInfoController]("cvinfo.fxml")

      TabManager().open("CV", {
        new Tab() {
          text = "CV"
          content = loader.node
          graphic = (new ImageView(LocalRes.CV_16_PNG))
        }
      }) {
        loader.controller.load()
      }
    }

    linkSearch.setOnAction { _ =>
      val loader = new SimpleFxmlLoader[SearchController]("search.fxml")

      TabManager().open("Search", {
        new Tab {
          text = "Search"
          content = loader.node
        }
      }) {
        loader.controller.title.set("")
      }
    }

    linkTags.setOnAction { _ =>

      TabManager().open("Tags",
        new DataPage(new TagView()) {
          text = "Tags"
        }
      )

    }

    linkBrand.setOnAction { _ =>
      val loader = new SimpleFxmlLoader[MainPanelController]("mainpanel.fxml")

      TabManager().open("Brand", {
        new Tab {
          text = "Brand"
          content = loader.node
        }
      }) {
        loader.controller.load()
      }

    }

  }


  @FXML private def miCloseOther_OnAction(actionEvent: ActionEvent) = {
    TabManager().closeOther()
  }

  @FXML private def miCloseRight_OnAction(actionEvent: ActionEvent) = {
    TabManager().closeRight()
  }

  @FXML private def miCloseLeft_OnAction(actionEvent: ActionEvent) = {
    TabManager().closeLeft()
  }

  @FXML private def miReload_OnAction(actionEvent: ActionEvent) = {
    TabManager().reloadActiveTabData()
  }

}