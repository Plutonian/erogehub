package com.goexp.galgame.gui.view.guide

import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.gui.task.game.search.sub.GuideSearchTask
import com.goexp.galgame.gui.util.Websites
import com.goexp.ui.javafx.control.cell.NodeListCell
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.input.{DragEvent, TransferMode}
import javafx.scene.layout.BorderPane

class SearchGuideController extends DefaultController {

  private lazy val searchKey = new SimpleStringProperty()

  @FXML private var textSearchGameKey: TextField = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _
  final private val guideListView = new ListView[GameGuide]
  final private val guideService = TaskService(new GuideSearchTask(searchKey.get()))

  override protected def initialize() = {

    searchKey.bindBidirectional(textSearchGameKey.textProperty())

    guideListView.itemsProperty().bind(guideService.valueProperty())

    guideListView.setCellFactory(_ => {
      var guide: GameGuide = null

      val link = new Hyperlink()
      link.setOnAction(_ => Websites.open(guide.href))

      NodeListCell[GameGuide] { g =>
        guide = g

        link.setText(s"[${guide.from}] ${guide.title}")
        link
      }
    })

    btnSearchGame.disableProperty.bind(textSearchGameKey.textProperty.isEmpty)
  }

  def load(title: String = ""): Unit = {
    searchKey.set(title)
  }

  @FXML private def btnSearchGame_OnAction(actionEvent: ActionEvent) = {
    searchPanel.setCenter(guideListView)
    guideService.restart()

  }

  @FXML private def textSearchGameKey_OnDragOver(e: DragEvent) = {
    val board = e.getDragboard
    val files = board.getFiles
    if (files.size == 1) e.acceptTransferModes(TransferMode.LINK)
  }

  @FXML private def textSearchGameKey_OnDragDropped(e: DragEvent) = {
    val board = e.getDragboard
    val files = board.getFiles
    if (files.size > 0) {
      val f = files.get(0)
      val title = f.getName.replaceFirst("""\.[^.]+""", "")

      searchKey.set(title)
    }
  }
}