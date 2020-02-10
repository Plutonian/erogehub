package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.gui.model.Brand
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
import javafx.scene.layout.Region

class CommonInfoTabController() extends DefaultController {
  init()
  var node: Region = _

  private var controller: InfoController = _

  private def init() = {
    val loader = new FXMLLoaderProxy[Region, InfoController](classOf[InfoController].getResource("info.fxml"))

    node = loader.node
    controller = loader.controller
    controller.dataViewController.tableViewController.tableColBrand.setVisible(false)
  }

  def load(brand: Brand) =
    controller.load(brand)

  override protected def initialize() = {
  }
}