package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.task.CVListTask
import com.goexp.galgame.gui.util.Datas
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.common.control.DataTableColumn
import com.goexp.ui.javafx.TaskService
import com.goexp.ui.javafx.control.cell.NodeTableCell
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label, TableColumn, TableView}
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

import java.time.LocalDate

class CVView extends TableView[CV] with Datas {

  setPrefHeight(651.0)
  setPrefWidth(1120.0)
  setTableMenuButtonVisible(true)

  getColumns.setAll(
    new TableColumn[CV, Int]("Star") {
      setPrefWidth(120)
      setEditable(false)
      setResizable(false)

      setCellValueFactory(param => new SimpleObjectProperty(param.getValue.star))
      setCellFactory { _ =>
        val image = LocalRes.HEART_16_PNG

        NodeTableCell { star =>
          if (star > 0) {
            val stars = (0 until star).to(LazyList)
              .map { _ => new ImageView(image) }
              .toArray
            new HBox(stars: _*)
          }
          else new Label(star.toString)
        }
      }
    },
    new TableColumn[CV, String]("Name") {
      setPrefWidth(120)
      setEditable(false)

      setCellValueFactory(param => new SimpleStringProperty(param.getValue.name))

      setCellFactory { _ =>

        NodeTableCell { name =>
          new Hyperlink() {
            setText(name)
            setOnAction(_ => HGameApp.loadCVTab(name, true))
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
  itemsProperty().bind(loadCVService.valueProperty())


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
