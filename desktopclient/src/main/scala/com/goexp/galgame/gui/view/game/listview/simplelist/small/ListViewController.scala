package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.ui.javafx.control.cell.NodeListCell
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.Region

class ListViewController extends DefaultController {
  @FXML var smallListSimple: ListView[Game] = _
  @FXML var detailPage: Region = _
  @FXML var detailPageController: OutPageController = _


  override protected def initialize() = {

    smallListSimple.setCellFactory(_ => {

      NodeListCell[Game] { game =>
        val loader = new FXMLLoaderProxy[Region, HeaderController]("header.fxml")
        loader.controller.load(game)
        loader.node
      }
    })


    smallListSimple.getSelectionModel.selectedItemProperty().addListener { (_, _, n) =>

      if (n != null) {
        detailPageController.load(n)
        detailPage.setVisible(true)
      }

    }

    detailPage.setVisible(false)

  }

}