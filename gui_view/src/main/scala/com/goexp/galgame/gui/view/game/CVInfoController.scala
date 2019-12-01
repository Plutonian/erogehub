package com.goexp.galgame.gui.view.game

import java.time.LocalDate

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.task.{CVListTask, TaskService}
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Hyperlink, TableCell, TableColumn, TableView}
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class CVInfoController extends DefaultController {
  @FXML private var tableCV: TableView[CV] = _
  @FXML private var colName: TableColumn[CV, String] = _
  @FXML private var colStar: TableColumn[CV, Int] = _
  @FXML private var colTag: TableColumn[CV, List[String]] = _
  @FXML private var colStart: TableColumn[CV, LocalDate] = _
  @FXML private var colEnd: TableColumn[CV, LocalDate] = _
  @FXML private var colSize: TableColumn[CV, Int] = _

  final private val loadCVService = TaskService(new CVListTask())

  override protected def initialize() = {
    colName.setCellValueFactory(new PropertyValueFactory[CV, String]("name"))
    colStar.setCellValueFactory(new PropertyValueFactory[CV, Int]("star"))
    colStart.setCellValueFactory(new PropertyValueFactory[CV, LocalDate]("start"))
    colEnd.setCellValueFactory(new PropertyValueFactory[CV, LocalDate]("end"))
    colSize.setCellValueFactory(new PropertyValueFactory[CV, Int]("size"))
    colTag.setCellValueFactory(new PropertyValueFactory[CV, List[String]]("tag"))

    colStar.setCellFactory(_ => {
      val image = LocalRes.HEART_16_PNG

      new TableCell[CV, Int]() {
        override protected def updateItem(star: Int, empty: Boolean) = {
          super.updateItem(star, empty)
          this.setGraphic(null)
          this.setText(null)
          if (!empty)
            if (star > 0) {
              val stars = Range(0, star).to(LazyList).map { _ => new ImageView(image) }.toArray

              val starBox = new HBox(stars: _*)

              this.setGraphic(starBox)
            }
            else this.setText(star.toString)
        }
      }
    })
    colName.setCellFactory(_ => new TableCell[CV, String]() {
      override protected def updateItem(item: String, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        this.setText(null)
        if (item != null && !empty) {
          val link = new Hyperlink(item)
          link.setOnAction(_ => HomeController.$this.loadCVTab(item, true))
          this.setGraphic(link)
        }
      }
    })
    colStart.setCellFactory(_ => new TableCell[CV, LocalDate]() {
      override protected def updateItem(item: LocalDate, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setText(null)
        this.setGraphic(null)
        if (item != null && !empty) this.setText(String.valueOf(item.getYear))
      }
    })
    colEnd.setCellFactory(_ => new TableCell[CV, LocalDate]() {
      override protected def updateItem(item: LocalDate, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setText(null)
        this.setGraphic(null)
        if (item != null && !empty) this.setText(String.valueOf(item.getYear))
      }
    })
    colTag.setCellFactory(_ => new TableCell[CV, List[String]]() {
      override protected def updateItem(tag: List[String], empty: Boolean) = {
        super.updateItem(tag, empty)
        this.setGraphic(null)
        this.setText(null)
        if (tag != null && !empty) {
          val hbox = new HBox
          hbox.setSpacing(5)
          hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
          this.setGraphic(hbox)
        }
      }
    })

    tableCV.itemsProperty().bind(loadCVService.valueProperty())

  }

  def load() = loadCVService.restart()
}