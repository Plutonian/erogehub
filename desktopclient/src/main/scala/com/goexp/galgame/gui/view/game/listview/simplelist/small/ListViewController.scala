package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.ui.javafx.DefaultController
import com.goexp.ui.javafx.control.cell.NodeListCell
import javafx.fxml.FXML
import javafx.scene.control.ListView

class ListViewController extends DefaultController {
  @FXML var smallListSimple: ListView[Game] = _


  override protected def initialize() = {

    smallListSimple.setCellFactory(_ => {

      val loader = new SimpleFxmlLoader[HeaderController]("header.fxml")

      NodeListCell[Game] { game =>

        loader.controller.load(game)
        loader.node
      }
    })
  }

}