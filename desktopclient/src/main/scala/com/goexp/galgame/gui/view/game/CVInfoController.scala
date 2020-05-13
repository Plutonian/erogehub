package com.goexp.galgame.gui.view.game

import java.time.LocalDate

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.task.CVListTask
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.Tags.maker
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.MainController
import com.goexp.ui.javafx.control.cell.{NodeTableCell, TextTableCell}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class CVInfoController extends DefaultController {
  @FXML private var tableCV: TableView[CV] = _
  @FXML private var colName: TableColumn[CV, String] = _
  @FXML private var colStar: TableColumn[CV, Int] = _
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

  final private val loadCVService = TaskService(new CVListTask())

  override protected def initialize() = {
    colPlayed.setText(GameState.PLAYED.name)
    colPlaying.setText(GameState.PLAYING.name)
    colHope.setText(GameState.HOPE.name)
    colViewLater.setText(GameState.READYTOVIEW.name)
    colUncheck.setText(GameState.UNCHECKED.name)


    colName.setCellValueFactory(param => new SimpleStringProperty(param.getValue.name))
    colStar.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.star))

    colStart.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.start))
    colEnd.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.end))

    colCount.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.count))
    colRealCount.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.realCount))
    colPlayed.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.played))
    colPlaying.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.playing))
    colHope.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.hope))
    colViewLater.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.viewLater))
    colUncheck.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.uncheck))


    colZero.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.zero))
    colOne.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.one))
    colTwo.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.two))
    colThree.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.three))
    colFour.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.four))
    colFive.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.five))


    colLocal.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.local))
    colNetdisk.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.netdisk))
    colRemote.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.remote))

    colTag.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.tag))

    colStar.setCellFactory { _ =>
      val image = LocalRes.HEART_16_PNG
      val box = new HBox()

      NodeTableCell { star =>
        if (star > 0) {
          val stars = Range(0, star).to(LazyList).map { _ => new ImageView(image) }.asJava
          box.getChildren.setAll(stars)
          box
        }
        else new Label(star.toString)
      }
    }

    colName.setCellFactory { _ =>
      val link = new Hyperlink()

      NodeTableCell { name =>
        link.setText(name)
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

    colTag.setCellFactory(_ => {
      val hbox = new HBox
      hbox.setSpacing(5)

      NodeTableCell { tag =>
        hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
        hbox
      }
    })

    tableCV.itemsProperty().bind(loadCVService.valueProperty())

  }

  def load() = loadCVService.restart()
}