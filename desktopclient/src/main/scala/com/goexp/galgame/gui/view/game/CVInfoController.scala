package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.util.Controller
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import scalafx.Includes._


class CVInfoController extends DefaultController with Controller {
  @FXML private var cvWebView: WebView = _

  object Page {
    def openCV(name: String) = {
      HGameApp.loadCVTab(name, true)
    }
  }


  override def load() = {
    // set js obj
    val webEngine = cvWebView.getEngine
    webEngine.documentProperty().onChange((_, _, newDoc) => {

      if (newDoc != null) {
        val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
        win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
      }

    })
    webEngine.load("http://localhost:9000/server/cv")

  }

}