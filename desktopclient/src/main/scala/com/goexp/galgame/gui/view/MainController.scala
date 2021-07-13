package com.goexp.galgame.gui.view

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.{ByCV, ByPainter}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.CommonInfoTabController
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.{CommonTabController, HomeController}
import com.goexp.galgame.gui.view.guide.SearchGuideController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.image.ImageView

class MainController extends DefaultController {

  @FXML var homeController: HomeController = _

  override protected def initialize(): Unit = {
    MainController.$this = this
  }


  def viewBrand(brand: Brand) = {
    val text = brand.name
    val conn = new CommonInfoTabController

    TabManager().open(text, {
      new Tab(text, conn.node) {
        setGraphic(new ImageView(LocalRes.BRAND_16_PNG))
      }
    }) {
      conn.load(brand)
    }

  }

  def loadPainterTab(painter: String) = {
    val conn = CommonTabController(new ByPainter(painter))

    TabManager().open(painter, {
      new Tab(painter, conn.node)
    }) {
      conn.load()
    }
  }

  def loadCVTab(cv: String, real: Boolean) = {
    val conn = CommonTabController(new ByCV(cv, real))

    TabManager().open(cv, {
      new Tab(cv, conn.node) {
        setGraphic(new ImageView(LocalRes.CV_16_PNG))
      }
    }) {
      conn.load()
    }

  }

  def loadDetail(game: Game) = {
    val loader = new SimpleFxmlLoader[OutPageController]("out_page.fxml")

    TabManager().open(game.name, {
      new Tab(game.name, loader.node) {
        setGraphic(new ImageView(LocalRes.GAME_16_PNG))
      }
    }) {
      loader.controller.load(game)
    }

  }

  def loadGuide(name: String) = {
    val title = s"攻略:${name}"
    val loader = new SimpleFxmlLoader[SearchGuideController]("searchguide.fxml")

    TabManager().open(title, {
      new Tab(title, loader.node)
    }) {

      loader.controller.load(name)
    }

  }
}

object MainController {

  def apply(): MainController = $this

  private var $this: MainController = _
}
