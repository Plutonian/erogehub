package com.goexp.galgame.gui

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.gui.util.FXMLLoaderProxy
import com.goexp.galgame.gui.util.res.gameimg.GameImages
import com.goexp.galgame.gui.view.MainController
import javafx.application.Application
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

object HGameApp extends App {
  var app: HGameApp = _

  //  def main(args: Array[String]): Unit =
  Application.launch(classOf[HGameApp])


}

class HGameApp extends Application {
  override def init(): Unit = {
    HGameApp.app = this
    Network.initProxy()
  }

  override def start(primaryStage: Stage): Unit = {
    val proxy = new FXMLLoaderProxy[Parent, MainController](getClass.getResource("HGameApp.fxml"))
    primaryStage.setTitle("エロゲ まとめ")
    primaryStage.setWidth(1400)
    primaryStage.setMinWidth(1200)
    primaryStage.setHeight(800)
    primaryStage.setMinHeight(800)
    primaryStage.setScene(new Scene(proxy.node, Color.BLACK))
    primaryStage.show()
  }

  override def stop(): Unit = {
    GameImages.executers.shutdown()
  }
}