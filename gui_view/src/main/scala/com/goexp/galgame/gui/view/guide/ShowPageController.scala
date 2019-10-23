package com.goexp.galgame.gui.view.guide

import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML
import javafx.scene.text.FontSmoothingType
import javafx.scene.web.WebView

class ShowPageController extends DefaultController {
  @FXML private var webView: WebView = _

  /**
    * Event
    */
  override protected def initialize() = {
    webView.setFontSmoothingType(FontSmoothingType.GRAY)
    webView.getEngine.getLoadWorker.messageProperty.addListener((_, _, newValue) => println(s"Mess:$newValue"))

    webView.getEngine.getLoadWorker.exceptionProperty.addListener((_, _, t1) => {
      if (t1 != null) println(s"Received exception: ${t1.getMessage}")
    })

    webView.getEngine.getLoadWorker.stateProperty.addListener((_, _, newValue) => {
      println(newValue)
    }) // addListener()
  }

  def load(html: String) = webView.getEngine.loadContent(html)
}