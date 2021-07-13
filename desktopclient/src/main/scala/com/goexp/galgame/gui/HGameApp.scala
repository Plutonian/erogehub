package com.goexp.galgame.gui

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.HGameApp.app
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.{ByCV, ByPainter}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.CommonInfoTabController
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.{CommonTabController, HomeController}
import com.goexp.galgame.gui.view.guide.SearchGuideController
import com.goexp.ui.javafx.FXMLLoaderProxy
import com.typesafe.scalalogging.Logger
import javafx.application.Application
import javafx.scene.control.Tab
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.util.function.Predicate

object HGameApp extends App {
  var app: HGameApp = _

  var DEFAULT_GAME_PREDICATE: Predicate[Game] = (g: Game) => !GameState.ignoreState().contains(g.state.get)

  def mergeP(p: Predicate[Game]) = {
    if (p != null) {
      if (DEFAULT_GAME_PREDICATE == null) {
        p
      } else {
        DEFAULT_GAME_PREDICATE.and(p)
      }
    } else {
      DEFAULT_GAME_PREDICATE
    }
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

  Application.launch(classOf[HGameApp])

}

class HGameApp extends Application {
  protected val logger = Logger(classOf[HGameApp])

  override def init(): Unit = {
    app = this
  }

  override def start(primaryStage: Stage): Unit = {

    logger.info("Starting App")

    val proxy = new FXMLLoaderProxy[Parent, HomeController]("home.fxml")
    primaryStage.setTitle("エロゲ まとめ")
    //    primaryStage.initStyle(StageStyle.UNDECORATED)
    primaryStage.setWidth(1400)
    primaryStage.setMinWidth(1200)
    primaryStage.setHeight(800)
    primaryStage.setMinHeight(800)
    primaryStage.setMaximized(true)
    primaryStage.setScene(new Scene(proxy.node, Color.BLACK))
    primaryStage.show()

    logger.info("Start App OK")
  }

  override def stop(): Unit = {
    logger.info("Stopping App")

    logger.info("Stop App OK")

  }
}