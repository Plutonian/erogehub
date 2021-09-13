package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.task.game.search.{ByName, ByNameEx}
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.{SimpleBooleanProperty, SimpleStringProperty}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, ToggleGroup}
import javafx.scene.input.{DragEvent, TransferMode}
import javafx.scene.layout.BorderPane

class SearchController extends DefaultController {
  final val onLoadProperty = new SimpleBooleanProperty(false)
  private lazy val key = new SimpleStringProperty()

  private var searchType: SearchType = _
  @FXML private var textSearchGameKey: TextField = _
  @FXML private var searchGroup: ToggleGroup = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _

  override protected def initialize() = {
    onLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val conn = ExplorerData(
          searchType match {
            case SearchType.Simple => new ByName(key.get())
            case SearchType.Extend => new ByNameEx(key.get())
            //            case SearchType.Full => new ByTag(key)
            case _ => null
          }
        )
        searchPanel.setCenter(conn)
        conn.load()
      }
    })
    btnSearchGame.disableProperty.bind(textSearchGameKey.textProperty.isEmpty)

    textSearchGameKey.textProperty().bindBidirectional(key)
  }

  def load(title: String = ""): Unit = {
    key.set(title)
    resetEvent()
  }

  private def resetEvent() = onLoadProperty.set(false)

  @FXML private def btnSearchGame_OnAction(actionEvent: ActionEvent) = {
    searchType = SearchType.from(searchGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt)
    onLoadProperty.set(true)
    resetEvent()
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

      key.set(title)
    }
  }
}