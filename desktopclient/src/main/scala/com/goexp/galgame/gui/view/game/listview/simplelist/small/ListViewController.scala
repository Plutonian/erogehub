package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.galgame.gui.view.common.control.listview.ReadOnlyCellSkin
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.ui.javafx.DefaultController
import com.goexp.ui.javafx.control.cell.NodeListCell
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.Region

class ListViewController extends DefaultController {
  @FXML var smallListSimple: ListView[Game] = _
  @FXML var detailPage: Region = _
  @FXML var detailPageController: OutPageController = _


  override protected def initialize() = {

    smallListSimple.setCellFactory(_ => {

      val loader = new SimpleFxmlLoader[HeaderController]("header.fxml")

      val cell = NodeListCell[Game] { game =>

        loader.controller.load(game)
        loader.node
      }
      cell.setSkin(new ReadOnlyCellSkin[Game](cell))
      cell
    })


    //    smallListSimple.getSelectionModel.selectedItemProperty().addListener { (_, _, n) =>
    //
    //      if (n != null) {
    //        detailPageController.load(n)
    //        detailPage.setVisible(true)
    //      }
    //
    //    }

    detailPage.setVisible(false)

  }

}