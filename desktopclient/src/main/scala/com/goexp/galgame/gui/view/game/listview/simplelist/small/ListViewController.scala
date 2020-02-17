package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
import com.goexp.ui.javafx.control.cell.NodeListCell
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.Region

class ListViewController extends DefaultController {
  @FXML private var smallListSimple: ListView[Game] = _

  override protected def initialize() =
    smallListSimple.setCellFactory(_ => {

      val loader = new FXMLLoaderProxy[Region, HeaderController]("header.fxml")

      NodeListCell[Game] { game =>
        loader.controller.load(game)
        loader.node
      }
    })
}