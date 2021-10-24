package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.task.CVListTask
import com.goexp.galgame.gui.util.Controller
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.common.control.DataTableColumn
import com.goexp.ui.javafx.TaskService
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.fxml.FXML
import scalafx.scene.control.{TableCell, TableColumn, TableView}
import scalafx.scene.layout.HBox
import javafx.scene.image.ImageView
import scalafx.Includes._
import scalafx.scene.control.{Hyperlink, Label}

import java.time.LocalDate

class CVView extends TableView[CV] with Controller {

  prefHeight = 651.0
  prefWidth = 1120.0
  tableMenuButtonVisible = true

  columns ++= Seq(
    new TableColumn[CV, Int]("Star") {
      prefWidth = (120)
      editable = (false)
      resizable = (false)

      cellValueFactory = param => new SimpleObjectProperty(param.value.star)
      cellFactory = (_: TableColumn[CV, Int]) => {
        val image = LocalRes.HEART_16_PNG

        new TableCell[CV, Int] {
          item.onChange { (_, _, star) => {
            graphic =
            //              if (star != null)
              if (star > 0) {
                new HBox {
                  children ++= (0 until star).map { _ => new ImageView(image) }
                }
              }
              else new Label {
                text = star.toString
              }
            //              else
            //                null
          }

          }
        }

      }
    },
    new TableColumn[CV, String]("Name") {
      prefWidth = (120)
      editable = (false)

      cellValueFactory = (param => new SimpleStringProperty(param.value.name))

      cellFactory = { (_: TableColumn[CV, String]) =>
        new TableCell[CV, String] {
          item.onChange { (_, _, name) =>
            graphic =
              if (name != null)
                new Hyperlink() {
                  text = name
                  onAction = _ => HGameApp.loadCVTab(name, true)
                }
              else
                null
          }
        }
      }

    },
    new DataTableColumn[CV]("Count") {
      setPrefWidth(60)
      setResizable(false)
      setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.five))

    }
  )


  final private val loadCVService = TaskService(new CVListTask())
  items <== loadCVService.valueProperty()


  @FXML private var colTag: TableColumn[CV, List[String]] = _


  @FXML var colStart: TableColumn[CV, LocalDate] = _
  @FXML var colEnd: TableColumn[CV, LocalDate] = _

  @FXML var colCount: TableColumn[CV, Int] = _
  @FXML var colRealCount: TableColumn[CV, Int] = _

  @FXML var colPlayed: TableColumn[CV, Int] = _
  @FXML var colPlaying: TableColumn[CV, Int] = _
  @FXML var colHope: TableColumn[CV, Int] = _
  @FXML var colViewLater: TableColumn[CV, Int] = _
  @FXML var colUncheck: TableColumn[CV, Int] = _

  @FXML var colZero: TableColumn[CV, Int] = _
  @FXML var colOne: TableColumn[CV, Int] = _
  @FXML var colTwo: TableColumn[CV, Int] = _
  @FXML var colThree: TableColumn[CV, Int] = _
  @FXML var colFour: TableColumn[CV, Int] = _
  @FXML var colFive: TableColumn[CV, Int] = _

  @FXML var colLocal: TableColumn[CV, Int] = _
  @FXML var colNetdisk: TableColumn[CV, Int] = _
  @FXML var colRemote: TableColumn[CV, Int] = _

  override def load(): Unit = {
    loadCVService.restart()
  }

}
