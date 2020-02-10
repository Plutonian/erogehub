package com.goexp.galgame.gui.view.game

import java.time.LocalDate

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.task.{CVListTask, TaskService}
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.MainController
import com.goexp.ui.javafx.control.cell.{NodeTableCell, TextTableCell}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import Tags.maker
import com.goexp.ui.javafx.DefaultController

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
    colName.setCellValueFactory(param => new SimpleStringProperty(param.getValue.name))
    colStar.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.star))
    colStart.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.start))
    colEnd.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.end))
    colSize.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.size))
    //    colTag.setCellValueFactory(new PropertyValueFactory[CV, List[String]]("tag"))
    colTag.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.tag))

    colStar.setCellFactory { _ =>
      val image = LocalRes.HEART_16_PNG

      NodeTableCell { star =>
        if (star > 0) {
          val stars = Range(0, star).to(LazyList).map { _ => new ImageView(image) }.toArray
          new HBox(stars: _*)
        }
        else new Label(star.toString)
      }
    }

    colName.setCellFactory { _ =>
      NodeTableCell { name =>
        val link = new Hyperlink(name)
        link.setOnAction(_ => MainController().loadCVTab(name, true))
        link

      }
    }

    colStart.setCellFactory(_ =>
      TextTableCell { startDate =>
        startDate.getYear.toString
      }
    )

    colEnd.setCellFactory(_ =>
      TextTableCell { endDate =>
        endDate.getYear.toString
      }
    )

    colTag.setCellFactory(_ =>
      NodeTableCell { tag =>
        val hbox = new HBox
        hbox.setSpacing(5)
        hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
        hbox
      }
    )

    tableCV.itemsProperty().bind(loadCVService.valueProperty())

  }

  def load() = loadCVService.restart()
}