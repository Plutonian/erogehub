package com.goexp.galgame.gui

import java.util.function.Predicate

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.gui.HGameApp.app
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.MainController
import com.goexp.ui.javafx.FXMLLoaderProxy
import com.typesafe.scalalogging.Logger
import javafx.application.Application
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, StageStyle}

object HGameApp extends App {
  var app: HGameApp = _

  var DEFAULT_GAME_PREDICATE: Predicate[Game] = (g: Game) => (g.state.get ne GameState.SAME) &&
    (g.state.get ne GameState.BLOCK)

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

  Application.launch(classOf[HGameApp])

}

class HGameApp extends Application {
  protected val logger = Logger(classOf[HGameApp])

  override def init(): Unit = {
    app = this
    Network.initProxy()
  }

  override def start(primaryStage: Stage): Unit = {

    logger.info("Starting App")

    val proxy = new FXMLLoaderProxy[Parent, MainController](getClass.getResource("HGameApp.fxml"))
    primaryStage.setTitle("エロゲ まとめ")
    primaryStage.initStyle(StageStyle.UNDECORATED)
    //    primaryStage.setWidth(1400)
    //    primaryStage.setMinWidth(1200)
    //    primaryStage.setHeight(800)
    //    primaryStage.setMinHeight(800)
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