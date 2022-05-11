package com.goexp.galgame.gui.view.game.explorer.sidebar

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.ByBrand
import com.goexp.galgame.gui.task.game.panel.group.node.{BrandItem, CompItem, DataItem}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{Controller, DataSource}
import com.goexp.ui.javafx.TaskService
import scalafx.Includes._
import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Pos
import scalafx.scene.control.{Label, TreeCell, TreeView}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{HBox, VBox}

import java.util
import java.util.function.Predicate

class BrandGroupView extends TreeView[DataItem] with Controller {
  showRoot = false

  final val onSetProperty = new BooleanProperty()

  private var filteredGames: util.List[Game] = _

  var selectedBrand: DataItem = _

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

                new HBox {
                  spacing = 5
                  children = Seq(
                    //                    new ImageView(LocalRes.BRAND_16_PNG) {
                    //                      alignment = Pos.CenterLeft
                    //                    },
                    //                    new Text {
                    //                      font = Font(32)
                    //
                    //                      text = "B"
                    //                    },
                    new HBox {

                      children = Seq(
                        new Label(s"${title} "),
                        new Label(s"(${count})")
                      )
                    }
                  )
                }
              //                new Label(s"${title} (${count})")
            }

          } else
            null

      }
      }
    }
  }

  selectionModel().selectedItem.onChange((_, _, item) => {
    if (item != null) {
      selectedBrand = item.getValue

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