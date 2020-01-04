package com.goexp.galgame.gui.view

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.{ByCV, ByPainter}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{FXMLLoaderProxy, TabSelect}
import com.goexp.galgame.gui.view.brand.CommonInfoTabController
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.{CommonTabController, HomeController}
import com.goexp.galgame.gui.view.guide.SearchGuideController
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.image.ImageView
import javafx.scene.layout.Region

class MainController extends DefaultController {

  @FXML var homeController: HomeController = _

  override protected def initialize(): Unit = {
    MainController.$this = this
  }

  def insertTab(tab: Tab, select: Boolean = true): Unit = {
    homeController.insertTab(tab, select)
  }

  def viewBrand(brand: Brand) = {
    val text = brand.name
    TabSelect().whenNotFound {
      val conn = new CommonInfoTabController
      val tab = new Tab(text, conn.node)
      tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG))
      conn.load(brand)
      tab
    }.select(text)
  }

  def loadPainterTab(painter: String) =
    TabSelect().whenNotFound {
      val conn = CommonTabController(new ByPainter(painter))
      val tab = new Tab(painter, conn.node)
      //                    tab.setGraphic(new ImageView(LocalRes.CV_16_PNG()));
      conn.load()
      tab
    }.select(painter)

  def loadCVTab(cv: String, real: Boolean) =
    TabSelect().whenNotFound {
      val conn = CommonTabController(new ByCV(cv, real))
      val tab = new Tab(cv, conn.node)
      tab.setGraphic(new ImageView(LocalRes.CV_16_PNG))
      conn.load()
      tab
    }.select(cv)

  def loadDetail(game: Game) =
    TabSelect().whenNotFound {
      val loader = new FXMLLoaderProxy[Region, OutPageController](MainController.GAME_DETAIL_NAV_PAGE_FXML)
      val tab = new Tab(game.name, loader.node)
      tab.setGraphic(new ImageView(LocalRes.GAME_16_PNG))
      loader.controller.load(game)
      tab
    }.select(game.name)

  def loadGuide(name: String) = {
    val title = s"攻略:${name}"
    TabSelect().whenNotFound {
      val loader = new FXMLLoaderProxy[Region, SearchGuideController](MainController.VIEW_GUIDE_PANEL_FXML)
      val tab = new Tab(title, loader.node)
      loader.controller.load(name)
      tab
    }.select(title)
  }
}

object MainController {
  private val GAME_DETAIL_NAV_PAGE_FXML = classOf[OutPageController].getResource("out_page.fxml")
  private val VIEW_GUIDE_PANEL_FXML = classOf[SearchGuideController].getResource("searchguide.fxml")

  def apply(): MainController = $this

  private var $this: MainController = _
}
