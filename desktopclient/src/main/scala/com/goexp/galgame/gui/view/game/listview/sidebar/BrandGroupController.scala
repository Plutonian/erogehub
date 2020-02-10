package com.goexp.galgame.gui.view.game.listview.sidebar

import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.TaskService
import com.goexp.galgame.gui.task.game.panel.group.ByBrand
import com.goexp.galgame.gui.task.game.panel.group.node.{BrandItem, CompItem, DataItem}
import com.goexp.ui.javafx.control.cell.NodeTreeCell
import javafx.fxml.FXML
import javafx.scene.control.{Label, TreeView}
import javafx.scene.layout.VBox

class BrandGroupController extends FilterController[Game] {
  @FXML private var compTree: TreeView[DataItem] = _
  private var filteredGames: util.List[Game] = _

  final private val groupBrandServ = TaskService(new ByBrand(filteredGames))

  override protected def initialize() = {
    compTree.setCellFactory(_ =>
      NodeTreeCell[DataItem] {
        case CompItem(title, count, _) =>
          new Label(s"${title} (${count})")
        case BrandItem(title, count, brand) =>
          new VBox(
            new Label(s"${title} (${count})"),
            {
              val label = new Label(brand.state.name)
              label.setStyle("-fx-text-fill:grey;")
              label
            })


      }
      //      TextTreeCell[DefaultItem] { item =>
      //        s"${item.title} (${item.count})"
      //      }
    )

    compTree.getSelectionModel.selectedItemProperty.addListener((_, _, item) => {
      if (item != null) {
        predicate =
          item.getValue match {
            case CompItem(_, _, acomp) =>
              (game: Game) => {

                val comp = Option(game.brand.comp).getOrElse("")
                comp == acomp
              }
            case BrandItem(_, _, abrand) =>
              (game: Game) => game.brand == abrand
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