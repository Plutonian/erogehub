package com.goexp.galgame.gui.view.game.listview.imglist

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.FXMLLoaderProxy
import com.goexp.galgame.gui.view.DefaultController
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.layout.Region
import org.controlsfx.control.{GridCell, GridView}

class ImgListViewController extends DefaultController {
  @FXML private var imgList: GridView[Game] = _

  override protected def initialize() =
    imgList.setCellFactory(_ => {
      val loader = new FXMLLoaderProxy[Region, CellController](classOf[CellController].getResource("cell.fxml"))
      new GridCell[Game]() {
        override protected def updateItem(game: Game, empty: Boolean) = {
          super.updateItem(game, empty)
          setGraphic(null)

          if (game != null && !empty) {
            loader.controller.load(game)
            setGraphic(loader.node)
          }
        }
      }
    })

  def load(item: ObservableList[Game]) = imgList.setItems(item)
}