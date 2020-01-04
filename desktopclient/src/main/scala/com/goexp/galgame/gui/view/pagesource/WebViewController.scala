package com.goexp.galgame.gui.view.pagesource

import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.view.DefaultController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import javafx.scene.text.FontSmoothingType
import javafx.scene.web.WebView

class WebViewController extends DefaultController {
  @FXML private var locationField: TextField = _
  @FXML private var lbUrl: Label = _
  @FXML private var webView: WebView = _

  override protected def initialize() = {
    webView.setFontSmoothingType(FontSmoothingType.GRAY)
    webView.getEngine.getLoadWorker.messageProperty.addListener((_, _, newValue) => println(s"Mess:$newValue"))

    webView.getEngine.getLoadWorker.exceptionProperty.addListener((_, _, t1) => {
      if (t1 != null) println(s"Received exception: ${t1.getMessage}")
    })

    webView.getEngine.getLoadWorker.stateProperty.addListener((_, _, newValue) => {
      println(newValue)
    }) // addListener()

    lbUrl.textProperty.bind(webView.getEngine.locationProperty)
    locationField.textProperty.bind(webView.getEngine.locationProperty)
  }

  def load(brand: Brand) = webView.getEngine.load(brand.website)

  @FXML private def goButton_OnAction(actionEvent: ActionEvent) = {
  }
}