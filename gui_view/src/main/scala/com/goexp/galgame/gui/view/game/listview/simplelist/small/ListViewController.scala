package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.FXMLLoaderProxy
import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.{ListCell, ListView}
import javafx.scene.layout.Region

class ListViewController extends DefaultController {
  @FXML private var smallListSimple: ListView[Game] = _

  override protected def initialize() =
    smallListSimple.setCellFactory(_ => {

      val loader = new FXMLLoaderProxy[Region, HeaderController](classOf[HeaderController].getResource("header.fxml"))

      new ListCell[Game]() {
        override protected def updateItem(item: Game, empty: Boolean) = {
          super.updateItem(item, empty)
          setGraphic(null)
          setText(null)
          if (item != null && !empty) {
            loader.controller.load(item)
            setGraphic(loader.node)
          }
        }
      }
    })
}