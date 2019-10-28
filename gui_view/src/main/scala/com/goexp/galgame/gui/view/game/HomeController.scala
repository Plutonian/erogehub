package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search._
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{FXMLLoaderProxy, TabSelect}
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.brand.{CommonInfoTabController, MainPanelController}
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.guide.SearchGuideController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Accordion, Hyperlink, Tab, TabPane}
import javafx.scene.image.ImageView
import javafx.scene.input.{DragEvent, TransferMode}
import javafx.scene.layout.{Region, VBox}

import scala.jdk.CollectionConverters._

object HomeController {
  private val GAME_DETAIL_NAV_PAGE_FXML = classOf[OutPageController].getResource("out_page.fxml")
  private val SEARCH_TYPE_FXML = classOf[TagController].getResource("tag.fxml")
  private val BRAND_PANEL_FXML = classOf[MainPanelController].getResource("mainpanel.fxml")
  private val SEARCH_FXML = classOf[SearchController].getResource("search.fxml")
  private val CVINFO_FXML = classOf[CVInfoController].getResource("cvinfo.fxml")
  private val VIEW_GUIDE_PANEL_FXML = classOf[SearchGuideController].getResource("searchguide.fxml")
  var $this: HomeController = _
}

class HomeController extends DefaultController {
  @FXML var mainTabPanel: TabPane = _
  @FXML private var dateController: DateController = _
  @FXML private var menuPanel: Accordion = _
  @FXML private var date: Region = _
  @FXML private var gameStateLinkPanel: VBox = _
  @FXML private var gameStateLikeLinkPanel: VBox = _
  @FXML private var linkDate: Hyperlink = _
  @FXML private var linkCV: Hyperlink = _
  @FXML private var linkSearch: Hyperlink = _
  @FXML private var linkTags: Hyperlink = _

  def insertTab(tab: Tab, select: Boolean = true): Unit = {
    val index = mainTabPanel.getSelectionModel.getSelectedIndex
    mainTabPanel.getTabs.add(index + 1, tab)
    if (select) mainTabPanel.getSelectionModel.select(tab)
  }

  override protected def initialize() = {
    HomeController.$this = this
    dateController.onLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val start = dateController.from
        val end = dateController.to
        val text = dateController.title
        TabSelect().ifNotFind(() => {
          val conn = new CommonTabController(() => new ByDateRange(start, end))
          val tab = new Tab(text, conn.node)
          tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          conn.load()
          tab
        }).select(text)
      }
    })
    dateController.onYearLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val from = dateController.from
        val to = dateController.to
        val text = dateController.title
        TabSelect().ifNotFind(() => {
          val conn = new CommonTabController(() => new ByDateRange(from, to))
          val tab = new Tab(text, conn.node)
          tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          conn.load()
          tab
        }).select(text)
      }
    })

    date.setVisible(false)
    menuPanel.setExpandedPane(menuPanel.getPanes.get(0))
    initBlockList()

    val links = gameType2Link(List(GameState.READYTOVIEW, GameState.HOPE, GameState.PLAYING)).asJava

    gameStateLikeLinkPanel.getChildren.setAll(links)
    linkDate.setGraphic(new ImageView(LocalRes.IMG_DATE_PNG))
    linkCV.setGraphic(new ImageView(LocalRes.IMG_CV_PNG))
    linkSearch.setGraphic(new ImageView(LocalRes.IMG_search_PNG))
    linkTags.setGraphic(new ImageView(LocalRes.IMG_TAG_PNG))
  }

  private def initBlockList() = {
    val links = gameType2Link(List(GameState.PLAYED)).asJava
    gameStateLinkPanel.getChildren.setAll(links)
  }


  private def gameType2Link(gameState: List[GameState]): LazyList[Hyperlink] =
    gameState.to(LazyList)
      .map(state => {
        val link = new Hyperlink
        link.setText(state.name)
        link.setUserData(state)
        link.setOnAction(_ => {
          val conn = new CommonTabController(() => new ByState(state))
          conn.controller.tableViewController.tableColStar.setVisible(false)
          conn.controller.tableViewController.tableColState.setVisible(false)
          val text = state.name
          TabSelect().ifNotFind(() => {
            val tab = new Tab(text, conn.node)
            conn.load()
            tab

          }).select(text)
        })
        link
      })

  def viewBrand(brand: Brand) = {
    val text = brand.name
    TabSelect().ifNotFind(() => {
      val conn = new CommonInfoTabController
      val tab = new Tab(text, conn.node)
      tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG))
      conn.load(brand)
      tab
    }).select(text)
  }

  def loadPainterTab(painter: String) =
    TabSelect().ifNotFind(() => {
      val conn = new CommonTabController(() => new ByPainter(painter))
      val tab = new Tab(painter, conn.node)
      //                    tab.setGraphic(new ImageView(LocalRes.CV_16_PNG()));
      conn.load()
      tab
    }).select(painter)

  def loadCVTab(cv: String, real: Boolean) =
    TabSelect().ifNotFind(() => {
      val conn = new CommonTabController(() => new ByCV(cv, real))
      val tab = new Tab(cv, conn.node)
      tab.setGraphic(new ImageView(LocalRes.CV_16_PNG))
      conn.load()
      tab
    }).select(cv)

  def loadDetail(game: Game) =
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, OutPageController](HomeController.GAME_DETAIL_NAV_PAGE_FXML)
      val tab = new Tab(game.name, loader.node)
      tab.setGraphic(new ImageView(LocalRes.GAME_16_PNG))
      loader.controller.load(game)
      tab
    }).select(game.name)

  def loadGuide(name: String) = {
    val title = s"攻略:${name}"
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, SearchGuideController](HomeController.VIEW_GUIDE_PANEL_FXML)
      val tab = new Tab(title, loader.node)
      loader.controller.load(name)
      tab
    }).select(title)
  }

  @FXML private def linkSearch_OnAction(actionEvent: ActionEvent) =
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, SearchController](HomeController.SEARCH_FXML)
      val tab = new Tab("Search", loader.node)
      loader.controller.load()
      tab
    }).select("Search")

  @FXML private def linkSearch_OnDragOver(e: DragEvent) = {
    val board = e.getDragboard
    val files = board.getFiles
    if (files.size == 1) e.acceptTransferModes(TransferMode.LINK)
  }

  @FXML private def linkSearch_OnDragDropped(e: DragEvent) = {
    val board = e.getDragboard
    val files = board.getFiles
    if (files.size > 0) {
      val f = files.get(0)
      val title = f.getName.replaceFirst("""\.[^.]+""", "")
      TabSelect().ifNotFind(() => {
        val loader = new FXMLLoaderProxy[Region, SearchController](HomeController.SEARCH_FXML)
        val tab = new Tab("Search", loader.node)
        loader.controller.load(title)
        tab
      }).select("Search")
    }
  }

  @FXML private def linkTags_OnAction(actionEvent: ActionEvent) =
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, TagController](HomeController.SEARCH_TYPE_FXML)
      val tab = new Tab("Tags", loader.node)
      loader.controller.load()
      tab

    }).select("Tags")

  @FXML private def linkDate_OnAction(actionEvent: ActionEvent) = {
    switchVisiable(date)
    if (date.isVisible) dateController.load()
  }

  @FXML private def linkBrand_OnAction(actionEvent: ActionEvent) =
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, MainPanelController](HomeController.BRAND_PANEL_FXML)
      val tab = new Tab("Brand", loader.node)
      loader.controller.load()
      tab
    }).select("Brand")

  @FXML private def linkCV_OnAction(actionEvent: ActionEvent) =
    TabSelect().ifNotFind(() => {
      val loader = new FXMLLoaderProxy[Region, CVInfoController](HomeController.CVINFO_FXML)
      loader.controller.load()
      val tab = new Tab("CV", loader.node)
      tab.setGraphic(new ImageView(LocalRes.CV_16_PNG))
      tab
    }).select("CV")

  @FXML private def pass_OnAction(actionEvent: ActionEvent) = {
    val title = "差"
    TabSelect().ifNotFind(() => {
      val conn = new CommonTabController(() => new ByStarRange(1, 2))
      conn.controller.tableViewController.tableColState.setVisible(false)
      val tab = new Tab(title, conn.node)
      conn.load(_.star < 3)
      tab
    }).select(title)
  }

  @FXML private def like_OnAction(actionEvent: ActionEvent) = {
    val title = "优"
    TabSelect().ifNotFind(() => {
      val conn = new CommonTabController(() => new ByStarRange(4, 5))
      conn.controller.tableViewController.tableColState.setVisible(false)
      val tab = new Tab(title, conn.node)
      conn.load((g: Game) => g.star > 3)
      tab
    }).select(title)
  }

  @FXML private def normal_OnAction(actionEvent: ActionEvent) = {
    val title = "良"
    TabSelect().ifNotFind(() => {
      val conn = new CommonTabController(() => new ByStarRange(3, 3))
      conn.controller.tableViewController.tableColState.setVisible(false)
      val tab = new Tab(title, conn.node)
      conn.load((g: Game) => g.star == 3)
      tab
    }).select(title)
  }

  @FXML private def miCloseOther_OnAction(actionEvent: ActionEvent) = {
    val tabs = mainTabPanel.getTabs.asScala.to(LazyList)
      .filter(_ ne mainTabPanel.getSelectionModel.getSelectedItem)
      .asJava
    mainTabPanel.getTabs.removeAll(tabs)
  }

  @FXML private def miCloseRight_OnAction(actionEvent: ActionEvent) =
    mainTabPanel.getTabs.remove(mainTabPanel.getSelectionModel.getSelectedIndex + 1, mainTabPanel.getTabs.size)

  private def switchVisiable(node: Node) = {
    node.setVisible(!node.isVisible)
    node.setManaged(!node.isManaged)
  }
}