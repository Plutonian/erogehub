package com.goexp.galgame.gui

import com.goexp.galgame.gui.HGameApp.app
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.InfoController
import com.goexp.galgame.gui.view.game.HomeController
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.guide.SearchView
import com.goexp.ui.javafx.FXMLLoaderProxy
import com.typesafe.scalalogging.Logger
import javafx.application.Application
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import scalafx.Includes._
import scalafx.scene.control.Tab
import scalafx.scene.image.ImageView

object HGameApp extends App {
  var app: HGameApp = _


  def viewBrand(brand: Brand) = {
    val t = brand.name
    val conn = new SimpleFxmlLoader[InfoController]("info.fxml")

    TabManager().open(t, {
      new Tab {
        text = t
        graphic = (new ImageView(LocalRes.BRAND_16_PNG))
        content = conn.node
      }
    }) {

      conn.controller.brand.value = brand
    }

  }


  def loadDetail(game: Game) = {

    //    val stage = new Stage()
    val loader = new SimpleFxmlLoader[OutPageController]("out_page.fxml")
    //    stage.setTitle(game.name)
    //    stage.setScene(new Scene(loader.node))
    //    loader.controller.load(game)
    //
    //    stage.show()

    TabManager().open(game.name, {
      new Tab {
        text = game.name

        graphic = (new ImageView(LocalRes.GAME_16_PNG))
        content = loader.node
      }
    }) {
      loader.controller.load(game)
    }

  }

  def loadGuide(name: String) = {
    val title = s"攻略:${name}"
    val view = new SearchView()

    TabManager().open(title, {
      new Tab {
        text = title
        content = view
      }

    }) {

      view.searchKey.set(name)
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