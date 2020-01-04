package com.goexp.galgame.gui.view.guide

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.search.sub.GuideSearchTask
import com.goexp.galgame.gui.util.{FXMLLoaderProxy, Websites}
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.javafx.cell.NodeListCell
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.input.{DragEvent, TransferMode}
import javafx.scene.layout.{BorderPane, HBox, Region}
import javafx.stage.Stage

class SearchGuideController extends DefaultController {

  private var key: String = _
  @FXML private var textSearchGameKey: TextField = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _
  final private val guideListView = new ListView[GameGuide]
  final private val guideService = TaskService(new GuideSearchTask(key))

  override protected def initialize() = {

    guideService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) guideListView.setItems(newValue)
    })

    guideListView.setCellFactory(_ => {
      val guideShowLoader = new FXMLLoaderProxy[Region, ShowPageController](classOf[ShowPageController].getResource("showpage.fxml"))


      NodeListCell[GameGuide] { guide =>
        val link = new Hyperlink(s"[${guide.from}] ${guide.title}")
        link.setOnAction(_ => Websites.open(guide.href))

        if (Strings.isNotEmpty(guide.html)) {

          val viewlink = new Hyperlink("View")
          viewlink.setOnAction { _ =>
            val view = new Stage
            view.setTitle(guide.title)
            view.setScene(new Scene(guideShowLoader.node))
            view.show()
            guideShowLoader.controller.load(guide.html)
          }

          val hBox = new HBox(link, viewlink)
          hBox.setSpacing(10)
          hBox
        }
        else
          link

      }
    })


    btnSearchGame.disableProperty.bind(textSearchGameKey.textProperty.isEmpty)
  }

  def load(): Unit = load("")

  def load(title: String): Unit = {
    textSearchGameKey.setText(title)
  }

  @FXML private def btnSearchGame_OnAction(actionEvent: ActionEvent) = {
    key = textSearchGameKey.getText.trim

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
      textSearchGameKey.setText(title)
    }
  }
}