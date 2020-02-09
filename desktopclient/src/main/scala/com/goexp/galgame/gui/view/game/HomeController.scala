package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.search._
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{FXMLLoaderProxy, TabSelect}
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.brand.MainPanelController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Accordion, Hyperlink, Tab, TabPane}
import javafx.scene.image.ImageView
import javafx.scene.input.TransferMode
import javafx.scene.layout.{Region, VBox}

import scala.jdk.CollectionConverters._

object HomeController {

  private val SEARCH_TYPE_FXML = classOf[TagController].getResource("tag.fxml")
  private val BRAND_PANEL_FXML = classOf[MainPanelController].getResource("mainpanel.fxml")
  private val SEARCH_FXML = classOf[SearchController].getResource("search.fxml")
  private val CVINFO_FXML = classOf[CVInfoController].getResource("cvinfo.fxml")
}

class HomeController extends DefaultController {
  @FXML private var dateController: DateController = _

  @FXML var mainTabPanel: TabPane = _

  @FXML private var menuPanel: Accordion = _
  @FXML private var date: Region = _
  @FXML private var gameStateLinkPanel: VBox = _
  @FXML private var gameStateLikeLinkPanel: VBox = _
  @FXML private var linkDate: Hyperlink = _

  @FXML private var linkLocal: Hyperlink = _
  @FXML private var linkNetDisk: Hyperlink = _

  @FXML private var linkCV: Hyperlink = _
  @FXML private var linkSearch: Hyperlink = _
  @FXML private var linkTags: Hyperlink = _
  @FXML private var linkBrand: Hyperlink = _

  @FXML private var linkGood: Hyperlink = _
  @FXML private var linkNormal: Hyperlink = _
  @FXML private var linkPass: Hyperlink = _


  override protected def initialize() = {

    def initBlockList() = {
      val links = gameState2Link(List(GameState.PLAYED)).asJava
      gameStateLinkPanel.getChildren.setAll(links)
    }


    def gameState2Link(gameState: List[GameState]): LazyList[Hyperlink] = {
      gameState.to(LazyList)
        .map(state => {
          val link = new Hyperlink
          link.setText(state.name)
          link.setUserData(state)
          link.setOnAction(_ => {
            val conn = CommonTabController(new ByState(state))
            conn.controller.tableViewController.tableColStar.setVisible(false)
            conn.controller.tableViewController.tableColState.setVisible(false)
            val text = state.name
            TabSelect()
              .whenNotFound(conn.load(), new Tab(text, conn.node))
              .select(text)
          })
          link
        })
    }

    dateController.onLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val start = dateController.from
        val end = dateController.to
        val text = dateController.title
        TabSelect().whenNotFound {
          val conn = CommonTabController(new ByDateRange(start, end))
          val tab = new Tab(text, conn.node)
          tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          conn.load()
          tab
        }.select(text)
      }
    })
    dateController.onYearLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val from = dateController.from
        val to = dateController.to
        val text = dateController.title
        TabSelect().whenNotFound {
          val conn = CommonTabController(new ByDateRange(from, to))
          val tab = new Tab(text, conn.node)
          tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          conn.load()
          tab
        }.select(text)
      }
    })

    date.setVisible(false)
    menuPanel.setExpandedPane(menuPanel.getPanes.get(0))
    initBlockList()

    val links = gameState2Link(List(GameState.READYTOVIEW, GameState.HOPE, GameState.PLAYING)).asJava
    gameStateLikeLinkPanel.getChildren.setAll(links)

    linkDate.setGraphic(new ImageView(LocalRes.IMG_DATE_PNG))
    linkDate.setOnAction { _ =>

      def switchVisiable(node: Node) = {
        node.setVisible(!node.isVisible)
        node.setManaged(!node.isManaged)
      }

      switchVisiable(date)
      if (date.isVisible) dateController.load()

    }

    linkCV.setGraphic(new ImageView(LocalRes.IMG_CV_PNG))
    linkCV.setOnAction { _ =>
      TabSelect().whenNotFound {
        val loader = new FXMLLoaderProxy[Region, CVInfoController](HomeController.CVINFO_FXML)
        loader.controller.load()
        val tab = new Tab("CV", loader.node)
        tab.setGraphic(new ImageView(LocalRes.CV_16_PNG))
        tab
      }.select("CV")

    }

    linkSearch.setGraphic(new ImageView(LocalRes.IMG_search_PNG))
    linkSearch.setOnAction { _ =>
      TabSelect().whenNotFound {
        val loader = new FXMLLoaderProxy[Region, SearchController](HomeController.SEARCH_FXML)
        val tab = new Tab("Search", loader.node)
        loader.controller.load()
        tab
      }.select("Search")
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
        TabSelect().whenNotFound {
          val loader = new FXMLLoaderProxy[Region, SearchController](HomeController.SEARCH_FXML)
          val tab = new Tab("Search", loader.node)
          loader.controller.load(title)
          tab
        }.select("Search")
      }

    }

    linkTags.setGraphic(new ImageView(LocalRes.IMG_TAG_PNG))
    linkTags.setOnAction { _ =>
      TabSelect().whenNotFound {
        val loader = new FXMLLoaderProxy[Region, TagController](HomeController.SEARCH_TYPE_FXML)
        val tab = new Tab("Tags", loader.node)
        loader.controller.load()
        tab

      }.select("Tags")

    }

    linkBrand.setOnAction { _ =>
      val loader = new FXMLLoaderProxy[Region, MainPanelController](HomeController.BRAND_PANEL_FXML)

      TabSelect()
        .whenNotFound(loader.controller.load(), new Tab("Brand", loader.node))
        .select("Brand")
    }

    linkGood.setOnAction { _ =>
      val title = "优"
      val conn = CommonTabController(new ByStarRange(4, 5))
      TabSelect().whenNotFound(conn.load((g: Game) => g.star > 3), {
        conn.controller.tableViewController.tableColState.setVisible(false)
        new Tab(title, conn.node)
      }).select(title)

    }

    linkNormal.setOnAction { _ =>
      val title = "良"
      val conn = CommonTabController(new ByStarRange(3, 3))

      TabSelect().whenNotFound {
        conn.controller.tableViewController.tableColState.setVisible(false)
        val tab = new Tab(title, conn.node)
        conn.load((g: Game) => g.star == 3)
        tab
      }.select(title)

    }

    linkPass.setOnAction { _ =>
      val title = "差"
      val conn = CommonTabController(new ByStarRange(1, 2))

      TabSelect().whenNotFound {
        conn.controller.tableViewController.tableColState.setVisible(false)
        val tab = new Tab(title, conn.node)
        conn.load(_.star < 3)
        tab
      }.select(title)

    }

    linkLocal.setOnAction { _ =>
      val title = "Local"
      val conn = CommonTabController(new ByLocation(GameLocation.LOCAL))
      TabSelect().whenNotFound {
        conn.controller.tableViewController.tableColState.setVisible(false)
        val tab = new Tab(title, conn.node)
        conn.load()
        tab
      }.select(title)

    }

    linkNetDisk.setOnAction { _ =>
      val title = "NetDisk"
      val conn = CommonTabController(new ByLocation(GameLocation.NETDISK))
      TabSelect().whenNotFound {
        conn.controller.tableViewController.tableColState.setVisible(false)
        val tab = new Tab(title, conn.node)
        conn.load()
        tab
      }.select(title)

    }

  }

  def insertTab(tab: Tab, select: Boolean = true): Unit = {

    val index = mainTabPanel.getSelectionModel.getSelectedIndex
    mainTabPanel.getTabs.add(index + 1, tab)
    if (select) mainTabPanel.getSelectionModel.select(tab)
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