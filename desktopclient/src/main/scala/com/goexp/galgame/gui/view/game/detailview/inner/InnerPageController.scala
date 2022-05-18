package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.common.website.{BangumiURL, WikiURL}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.VelocityTemplateConfig
import com.goexp.galgame.gui.{Config, HGameApp}
import com.goexp.ui.javafx.DefaultController
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import org.apache.velocity.VelocityContext

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


//    val root = new VelocityContext()
//
//    root.put("IMG_REMOTE", Config.IMG_REMOTE)
//    root.put("GetchuGameLocal", GetchuGameLocal)
//    root.put("LOCAL", GameLocation.LOCAL)
//    root.put("DateUtil", DateUtil)
//    root.put("Strings", Strings)
//    root.put("g", game)
//
//
//    val str = VelocityTemplateConfig
//      .tpl("/tpl/game/detail/index.html")
//      .process(root)

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