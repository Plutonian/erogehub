package com.goexp.galgame.gui

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.gui.HGameApp.app
import com.goexp.ui.javafx.FXMLLoaderProxy
import com.goexp.galgame.gui.view.MainController
import com.typesafe.scalalogging.Logger
import javafx.application.Application
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, StageStyle}

object HGameApp extends App {
  var app: HGameApp = _

  //  def main(args: Array[String]): Unit =
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