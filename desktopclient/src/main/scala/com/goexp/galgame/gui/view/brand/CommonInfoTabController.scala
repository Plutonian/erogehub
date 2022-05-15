package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.ui.javafx.DefaultController
import javafx.scene.layout.Region

class CommonInfoTabController() extends DefaultController {
  val loader = new SimpleFxmlLoader[InfoController]("info.fxml")

  private val controller: InfoController = loader.controller
  val node = loader.node

  def load(brand: Brand) =
    controller.load(brand)
}