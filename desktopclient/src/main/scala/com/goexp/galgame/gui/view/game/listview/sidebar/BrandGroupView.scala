package com.goexp.galgame.gui.view.game.listview.sidebar

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.ByBrand
import com.goexp.galgame.gui.task.game.panel.group.node.{BrandItem, CompItem, DataItem}
import com.goexp.galgame.gui.util.{Controller, DataSource}
import com.goexp.ui.javafx.TaskService
import scalafx.Includes._
import scalafx.beans.property.BooleanProperty
import scalafx.scene.control.{Label, TreeCell, TreeView}

import java.util
import java.util.function.Predicate

class BrandGroupView extends TreeView[DataItem] with Controller {
  showRoot = false


  final val onSetProperty = new BooleanProperty()
  var predicate: Predicate[Game] = _

  //  @FXML private var compTree: TreeView[DataItem] = _
  private var filteredGames: util.List[Game] = _

  object Data extends DataSource {
    val groupBrandServ = TaskService(new ByBrand(filteredGames))

    def load() = {
      groupBrandServ.restart()
    }
  }


  cellFactory = _ => {
    new TreeCell[DataItem] {
      item.onChange { (_, _, data) => {
        graphic =
          if (data != null) {
            data match {
              case CompItem(title, count, _) =>
                new Label(s"${title} (${count})")
              case BrandItem(title, count, brand) =>
                //          new VBox(
                new Label(s"${title} (${count})")
            }

          } else
            null

      }
      }
    }
  }

  selectionModel().selectedItem.onChange((_, _, item) => {
    if (item != null) {
      predicate =
        item.value() match {
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

  Data.groupBrandServ.value.onChange((_, _, newValue) => {
    if (newValue != null) this.setRoot(newValue)
  })

  this.filteredGames = filteredGames


  override def load(): Unit = {
    Data.load()
  }

  def init(filteredGames: util.List[Game]): Unit = {
    this.filteredGames = filteredGames
    load()
  }

}