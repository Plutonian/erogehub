package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.galgame.common.website.{BangumiURL, WikiURL}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Websites
import com.goexp.ui.javafx.DefaultController
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.web.WebView
import netscape.javascript.JSObject

class InnerPageController extends DefaultController {
  private var game: Game = _

  @FXML private var indexWebView: WebView = _

  object Page {

    def loadPainterTab(painter: String) = {
      val str = painter.replaceAll("（[^）]+）", "")
      HGameApp.loadPainterTab(str)
    }

    def loadCVTab(cv: String, real: Boolean) = {
      HGameApp.loadCVTab(cv, real)
    }

    def openWiki(cv: String) = {
      Websites.open(WikiURL.fromTitle(cv))
    }

    def openBangumi(cv: String) = {
      Websites.open(BangumiURL.fromTitle(cv))
    }

    def openBrand(id: Int) = {
      HGameApp.viewBrand(game.brand)
    }

    def openUrl(url: String) = {
      Websites.open(url)
    }

  }


  def load(game: Game): Unit = {
    require(game != null)

    this.game = game


    // set js obj
    val webEngine = indexWebView.getEngine
    webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
      if (newState eq Worker.State.SUCCEEDED) {
        val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
        win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
      }
    })
    webEngine.load(s"http://localhost:9000/game/${game.id}")

  }

}