package com.goexp.galgame.gui.view.game.listview.sidebar

import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.panel.group.ByBrand
import com.goexp.galgame.gui.task.game.panel.group.node.{BrandItem, CompItem, DefaultItem}
import com.goexp.javafx.cell.TextTreeCell
import javafx.fxml.FXML
import javafx.scene.control.TreeView

class BrandGroupController extends FilterController[Game] {
  @FXML private var compTree: TreeView[DefaultItem] = _
  private var filteredGames: util.List[Game] = _

  final private val groupBrandServ = TaskService(new ByBrand(filteredGames))

  override protected def initialize() = {
    compTree.setCellFactory(_ =>
      TextTreeCell[DefaultItem] { item =>
        s"${item.title} (${item.count})"
      }
    )

    compTree.getSelectionModel.selectedItemProperty.addListener((_, _, item) => {
      if (item != null) {
        predicate =
          item.getValue match {
            case compItem: CompItem =>
              (game: Game) => {

                val comp = Option(game.brand.comp).getOrElse("")
                comp == compItem.comp
              }
            case brandItem: BrandItem =>
              (game: Game) => game.brand == brandItem.brand
          }

        onSetProperty.set(true)
        onSetProperty.set(false)
      }
    })

    groupBrandServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) compTree.setRoot(newValue)
    })
  }

  override def init(filteredGames: util.List[Game]) = {
    this.filteredGames = filteredGames

    groupBrandServ.restart()
  }
}