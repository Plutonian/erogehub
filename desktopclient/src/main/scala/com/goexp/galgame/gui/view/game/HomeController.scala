package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.search._
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.MainPanelController
import com.goexp.galgame.gui.view.game.HomeController.{queryByLocationConfig2, queryByQuConfig2, queryLocalConfig, queryRemoteConfig}
import com.goexp.galgame.gui.view.game.listview.sidebar.FilterPanelController
import com.goexp.ui.javafx.DefaultController
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Tab, TabPane}
import javafx.scene.image.ImageView
import javafx.scene.input.TransferMode
import javafx.scene.layout.Pane
import org.controlsfx.control.PopOver
import org.controlsfx.control.PopOver.ArrowLocation

import scala.jdk.CollectionConverters._

class ConfigItem[T] {
  var title: String = _
  var icon: ImageView = _
  var dataTask: Task[ObservableList[T]] = _
}

private object HomeController {

  val queryByQuConfig2 = List(
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

  val queryByLocationConfig2 = List(
    new ConfigItem[Game] {
      title = GameLocation.LOCAL.name
      dataTask = new ByLocation(GameLocation.LOCAL)
    },
    new ConfigItem[Game] {
      title = GameLocation.NETDISK.name
      dataTask = new ByLocation(GameLocation.NETDISK)
    },
    new ConfigItem[Game] {
      title = GameLocation.REMOTE.name
      dataTask = new ByLocation(GameLocation.REMOTE)
    }
  )

  val queryLocalConfig = List(
    new ConfigItem[Game] {
      title = GameState.PLAYED.name
      dataTask = new ByState(GameState.PLAYED)
    },
    new ConfigItem[Game] {
      title = GameState.PLAYING.name
      dataTask = new ByState(GameState.PLAYING)
    }
  )

  val queryRemoteConfig = List(
    new ConfigItem[Game] {
      title = GameState.READYTOVIEW.name
      dataTask = new ByState(GameState.READYTOVIEW)
    },
    new ConfigItem[Game] {
      title = GameState.HOPE.name
      dataTask = new ByState(GameState.HOPE)
    }
  )

}

class HomeController extends DefaultController {

  @FXML var mainTabPanel: TabPane = _

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

  override protected def initialize() = {

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

    def configItemToNode(item: ConfigItem[Game]) = {
      new Hyperlink(item.title) {
        setGraphic(item.icon)
        setOnAction { _ =>
          val conn = CommonTabController(item.dataTask)

          TabManager().open(item.title, {
            conn.controller.tablelistController.tableColState.setVisible(false)
            new Tab(item.title, conn.node)
          }) {
            conn.load()
          }
        }
      }
    }


    {
      val loader = new SimpleFxmlLoader[DateController]("date.fxml")

      val popPanel = new PopOver {
        setArrowLocation(ArrowLocation.LEFT_TOP)
        setAutoHide(true)
        setContentNode(loader.node)
      }


      linkDate.setGraphic(new ImageView(LocalRes.IMG_DATE_PNG))
      linkDate.setOnAction { _ =>

        if (!popPanel.isShowing)
          popPanel.show(linkDate)

      }
    }

    {
      val loaderConfig = new SimpleFxmlLoader[FilterPanelController]("filterpanel.fxml")

      val popConfigPanel = new PopOver {
        setArrowLocation(ArrowLocation.BOTTOM_LEFT)
        setAutoHide(true)
        setContentNode(loaderConfig.node)
      }

      val controller = loaderConfig.controller
      controller.onSetProperty.addListener { (_, _, v) =>
        if (v) {
          HGameApp.DEFAULT_GAME_PREDICATE = controller.predicate
        }
      }

      linkConfig.setOnAction { _ =>

        if (!popConfigPanel.isShowing)
          popConfigPanel.show(linkConfig)

      }
    }


    {
      linkCV.setGraphic(new ImageView(LocalRes.IMG_CV_PNG))
      linkCV.setOnAction { _ =>
        val loader = new SimpleFxmlLoader[CVInfoController]("cvinfo.fxml")

        TabManager().open("CV", {
          new Tab("CV", loader.node) {
            setGraphic(new ImageView(LocalRes.CV_16_PNG))
          }
        }) {
          loader.controller.load()
        }
      }
    }

    {
      linkSearch.setGraphic(new ImageView(LocalRes.IMG_search_PNG))
      linkSearch.setOnAction { _ =>
        val loader = new SimpleFxmlLoader[SearchController]("search.fxml")

        TabManager().open("Search", {
          new Tab("Search", loader.node)
        }) {
          loader.controller.load()
        }
      }


      linkSearch.setOnDragOver { e =>
        val board = e.getDragboard
        val files = board.getFiles
        if (files.size == 1) e.acceptTransferModes(TransferMode.LINK)
      }
      linkSearch.setOnDragDropped { e =>
        val board = e.getDragboard
        val files = board.getFiles
        if (files.size > 0) {
          val f = files.get(0)
          val title = f.getName.replaceFirst("""\.[^.]+""", "")

          val loader = new SimpleFxmlLoader[SearchController]("search.fxml")

          TabManager().open("Search", {
            new Tab("Search", loader.node)
          }) {
            loader.controller.load(title)
          }
        }

      }
    }

    {
      linkTags.setGraphic(new ImageView(LocalRes.IMG_TAG_PNG))
      linkTags.setOnAction { _ =>
        val loader = new SimpleFxmlLoader[TagController]("tag.fxml")

        TabManager().open("Tags", {
          new Tab("Tags", loader.node)
        }) {
          loader.controller.load()
        }
      }
    }

    linkBrand.setOnAction { _ =>
      val loader = new SimpleFxmlLoader[MainPanelController]("mainpanel.fxml")

      TabManager().open("Brand", {
        new Tab("Brand", loader.node)
      }) {
        loader.controller.load()
      }

    }


    //    linkLocal.setOnAction { _ =>
    //      val title = "Local"
    //      val conn = CommonTabController(new ByLocation(GameLocation.LOCAL))
    //
    //      TabManager().open(title, {
    //        conn.controller.tablelistController.tableColState.setVisible(false)
    //        new Tab(title, conn.node)
    //      }) {
    //        conn.load()
    //      }
    //    }
    //
    //    linkNetDisk.setOnAction { _ =>
    //      val title = "NetDisk"
    //      val conn = CommonTabController(new ByLocation(GameLocation.NETDISK))
    //
    //      TabManager().open(title, {
    //        conn.controller.tablelistController.tableColState.setVisible(false)
    //        new Tab(title, conn.node)
    //      }) {
    //        conn.load()
    //      }
    //
    //    }
    //
    //    linkRemote.setOnAction { _ =>
    //      val title = "Remote"
    //      val conn = CommonTabController(new ByLocation(GameLocation.REMOTE))
    //
    //      TabManager().open(title, {
    //        conn.controller.tablelistController.tableColState.setVisible(false)
    //        new Tab(title, conn.node)
    //
    //      }) {
    //        conn.load()
    //      }
    //
    //    }

  }


  @FXML private def miCloseOther_OnAction(actionEvent: ActionEvent) = {
    val tabs = mainTabPanel.getTabs.asScala.to(LazyList)
      .filter(_ ne mainTabPanel.getSelectionModel.getSelectedItem)
      .asJava
    mainTabPanel.getTabs.removeAll(tabs)
  }

  @FXML private def miCloseRight_OnAction(actionEvent: ActionEvent) =
    mainTabPanel.getTabs.remove(mainTabPanel.getSelectionModel.getSelectedIndex + 1, mainTabPanel.getTabs.size)

}