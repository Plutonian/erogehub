package com.goexp.galgame.gui.view

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.{ByCV, ByPainter}
import com.goexp.galgame.gui.util.TabSelect
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.brand.CommonInfoTabController
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.{CommonTabController, HomeController}
import com.goexp.galgame.gui.view.guide.SearchGuideController
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
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
    val conn = new CommonInfoTabController

    TabSelect().whenNotFound(conn.load(brand), {
      val tab = new Tab(text, conn.node)
      tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG))
      tab
    }).select(text)
  }

  def loadPainterTab(painter: String) = {
    val conn = CommonTabController(new ByPainter(painter))

    TabSelect().whenNotFound(conn.load(), {
      val tab = new Tab(painter, conn.node)
      //                    tab.setGraphic(new ImageView(LocalRes.CV_16_PNG()));

      tab
    }).select(painter)
  }

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
      val loader = new FXMLLoaderProxy[Region, OutPageController]("out_page.fxml")
      val tab = new Tab(game.name, loader.node)
      tab.setGraphic(new ImageView(LocalRes.GAME_16_PNG))
      loader.controller.load(game)
      tab
    }.select(game.name)

  def loadGuide(name: String) = {
    val title = s"攻略:${name}"
    TabSelect().whenNotFound {
      val loader = new FXMLLoaderProxy[Region, SearchGuideController]("searchguide.fxml")
      val tab = new Tab(title, loader.node)
      loader.controller.load(name)
      tab
    }.select(title)
  }
}

object MainController {

  def apply(): MainController = $this

  private var $this: MainController = _
}
