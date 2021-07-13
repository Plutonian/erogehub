package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.ui.javafx.DefaultController
import javafx.scene.layout.Region

class CommonInfoTabController() extends DefaultController {
  init()
  var node: Region = _

  private var controller: InfoController = _

  private def init() = {
    val loader = new SimpleFxmlLoader[InfoController]("info.fxml")

    node = loader.node
    controller = loader.controller
    //    controller.dataViewController.tablelistController.tableColBrand.setVisible(false)
  }

  def load(brand: Brand) =
    controller.load(brand)

  override protected def initialize() = {
  }
}