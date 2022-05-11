package com.goexp.galgame.gui

import com.goexp.galgame.gui.HGameApp.app
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.{ByCV, ByPainter, ByTag}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, TabManager}
import com.goexp.galgame.gui.view.brand.CommonInfoTabController
import com.goexp.galgame.gui.view.common.control.DataPage
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.{ExplorerData, HomeController}
import com.goexp.galgame.gui.view.guide.SearchView
import com.goexp.ui.javafx.FXMLLoaderProxy
import com.typesafe.scalalogging.Logger
import javafx.application.Application
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.{Handler, Server, ServerConnector}
import org.eclipse.jetty.util.resource.Resource
import scalafx.Includes._
import scalafx.scene.control.Tab
import scalafx.scene.image.ImageView


object WebPageServer {

  private val server = new Server

  def start() = {
    import org.eclipse.jetty.server.handler.{HandlerList, ResourceHandler}


    val connector = new ServerConnector(server)
    connector.setPort(8080)
    server.addConnector(connector)

    val resource_handler = new ResourceHandler
    resource_handler.setDirectoriesListed(true)
    resource_handler.setWelcomeFiles(Array[String]("index.html"))

    //    resource_handler.set
    resource_handler.setBaseResource(Resource.newResource(this.getClass.getResource("/tpl")))

    val handlers = new HandlerList
    handlers.setHandlers(Array[Handler](resource_handler, new DefaultHandler))
    server.setHandler(handlers)

    server.start
  }

  def stop() = {
    server.stop()
  }


}

object HGameApp extends App {
  var app: HGameApp = _


  def viewBrand(brand: Brand) = {
    val t = brand.name
    val conn = new CommonInfoTabController

    TabManager().open(t, {
      new Tab {
        text = t
        graphic = (new ImageView(LocalRes.BRAND_16_PNG))
        content = conn.node
      }
    }) {
      conn.load(brand)
    }

  }

  def loadPainterTab(painter: String) = {

    TabManager().open(painter, {
      new DataPage(ExplorerData(new ByPainter(painter))) {
        text = (painter)
      }
    })
  }

  def loadCVTab(cv: String, real: Boolean) = {

    TabManager().open(cv, {
      new DataPage(ExplorerData(new ByCV(cv, real))) {
        text = (cv)
        graphic = (new ImageView(LocalRes.CV_16_PNG))
      }
    })
  }

  def loadDetail(game: Game) = {
    val loader = new SimpleFxmlLoader[OutPageController]("out_page.fxml")

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

  def openTag(tag: String) = {
    TabManager().open(tag,
      new DataPage(ExplorerData(new com.goexp.galgame.gui.task.game.search.ByTag(tag))) {
        text = (tag)
        graphic = (new ImageView(LocalRes.TAG_16_PNG))
      }
    )
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

      view.load(name)
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

    WebPageServer.start()

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

    WebPageServer.stop()

    logger.info("Stop App OK")

  }
}