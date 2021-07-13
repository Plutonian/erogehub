package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.task.game.search._
import com.goexp.galgame.gui.util.TabManager
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.brand.MainPanelController
import com.goexp.galgame.gui.view.game.listview.sidebar.FilterPanelController
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Tab, TabPane}
import javafx.scene.image.ImageView
import javafx.scene.input.TransferMode
import javafx.scene.layout.{Region, VBox}
import org.controlsfx.control.PopOver

import scala.jdk.CollectionConverters._

class HomeController extends DefaultController {

  @FXML var mainTabPanel: TabPane = _

  @FXML private var gameStateLinkPanel: VBox = _
  @FXML private var gameStateLikeLinkPanel: VBox = _
  @FXML private var linkDate: Hyperlink = _

  @FXML private var linkLocal: Hyperlink = _
  @FXML private var linkNetDisk: Hyperlink = _
  @FXML private var linkRemote: Hyperlink = _

  @FXML private var linkCV: Hyperlink = _
  @FXML private var linkSearch: Hyperlink = _
  @FXML private var linkTags: Hyperlink = _
  @FXML private var linkBrand: Hyperlink = _

  @FXML private var linkGood: Hyperlink = _
  @FXML private var linkNormal: Hyperlink = _
  @FXML private var linkPass: Hyperlink = _

  @FXML private var linkConfig: Hyperlink = _
  private val popPanel = new PopOver

  private val popConfigPanel = new PopOver


  override protected def initialize() = {

    def initBlockList() = {
      val links = state2Link(List(
        GameState.PLAYED,
        GameState.PLAYING
      )).asJava
      gameStateLinkPanel.getChildren.setAll(links)
    }


    def state2Link(gameState: List[GameState]): LazyList[Hyperlink] = {
      gameState.to(LazyList)
        .map(state => {
          val link = new Hyperlink
          link.setText(state.name)
          link.setUserData(state)
          link.setOnAction(_ => {
            val conn = CommonTabController(new ByState(state))
            //            conn.controller.tablelistController.tableColStar.setVisible(false)
            //            conn.controller.tablelistController.tableColState.setVisible(false)
            val text = state.name

            TabManager().open(text, {
              new Tab(text, conn.node)
            }) {
              conn.load()
            }

          })
          link
        })
    }



    //    date.setVisible(false)
    //    menuPanel.setExpandedPane(menuPanel.getPanes.get(0))
    initBlockList()

    val links = state2Link(List(GameState.READYTOVIEW, GameState.HOPE)).asJava
    gameStateLikeLinkPanel.getChildren.setAll(links)

    {
      val loader = new FXMLLoaderProxy[Region, DateController]("date.fxml")
      popPanel.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP)
      popPanel.setAutoHide(true)
      popPanel.setContentNode(loader.node)


      linkDate.setGraphic(new ImageView(LocalRes.IMG_DATE_PNG))
      linkDate.setOnAction { _ =>

        if (!popPanel.isShowing)
          popPanel.show(linkDate)

      }
    }

    {
      val loaderConfig = new FXMLLoaderProxy[Region, FilterPanelController]("filterpanel.fxml")
      popConfigPanel.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT)
      popConfigPanel.setAutoHide(true)
      popConfigPanel.setContentNode(loaderConfig.node)

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


    linkCV.setGraphic(new ImageView(LocalRes.IMG_CV_PNG))
    linkCV.setOnAction { _ =>
      val loader = new FXMLLoaderProxy[Region, CVInfoController]("cvinfo.fxml")

      TabManager().open("CV", {
        new Tab("CV", loader.node) {
          setGraphic(new ImageView(LocalRes.CV_16_PNG))
        }
      }) {
        loader.controller.load()
      }
    }

    linkSearch.setGraphic(new ImageView(LocalRes.IMG_search_PNG))
    linkSearch.setOnAction { _ =>
      val loader = new FXMLLoaderProxy[Region, SearchController]("search.fxml")

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

        val loader = new FXMLLoaderProxy[Region, SearchController]("search.fxml")

        TabManager().open("Search", {
          new Tab("Search", loader.node)
        }) {
          loader.controller.load(title)
        }
      }

    }

    linkTags.setGraphic(new ImageView(LocalRes.IMG_TAG_PNG))
    linkTags.setOnAction { _ =>
      val loader = new FXMLLoaderProxy[Region, TagController]("tag.fxml")

      TabManager().open("Tags", {
        new Tab("Tags", loader.node)
      }) {
        loader.controller.load()
      }
    }

    linkBrand.setOnAction { _ =>
      val loader = new FXMLLoaderProxy[Region, MainPanelController]("mainpanel.fxml")

      TabManager().open("Brand", {
        new Tab("Brand", loader.node)
      }) {
        loader.controller.load()
      }

    }

    linkGood.setOnAction { _ =>
      val title = "优"
      val conn = CommonTabController(new ByStarRange(4, 5))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }) {
        conn.load(_.star.get() > 3)
      }

    }

    linkNormal.setOnAction { _ =>
      val title = "良"
      val conn = CommonTabController(new ByStarRange(3, 3))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }) {
        conn.load(_.star.get() == 3)
      }
    }

    linkPass.setOnAction { _ =>
      val title = "差"
      val conn = CommonTabController(new ByStarRange(1, 2))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }) {
        conn.load(_.star.get() < 3)
      }
    }

    linkLocal.setOnAction { _ =>
      val title = "Local"
      val conn = CommonTabController(new ByLocation(GameLocation.LOCAL))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }) {
        conn.load()
      }
    }

    linkNetDisk.setOnAction { _ =>
      val title = "NetDisk"
      val conn = CommonTabController(new ByLocation(GameLocation.NETDISK))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }) {
        conn.load()
      }

    }

    linkRemote.setOnAction { _ =>
      val title = "Remote"
      val conn = CommonTabController(new ByLocation(GameLocation.REMOTE))

      TabManager().open(title, {
        conn.controller.tablelistController.tableColState.setVisible(false)
        new Tab(title, conn.node)

      }) {
        conn.load()
      }

    }

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