package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.task.game.search.{ByName, ByNameEx}
import com.goexp.galgame.gui.view.DefaultController
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, ToggleGroup}
import javafx.scene.input.{DragEvent, TransferMode}
import javafx.scene.layout.BorderPane

class SearchController extends DefaultController {
  final val onLoadProperty = new SimpleBooleanProperty(false)
  private var key: String = _
  private var searchType: SearchType = _
  @FXML private var textSearchGameKey: TextField = _
  @FXML private var searchGroup: ToggleGroup = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _

  override protected def initialize() = {
    onLoadProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val conn = CommonTabController(
          searchType match {
            case SearchType.Simple => new ByName(key)
            case SearchType.Extend => new ByNameEx(key)
            //            case SearchType.Full => new ByTag(key)
            case _ => null
          }
        )
        searchPanel.setCenter(conn.node)
        conn.load()
      }
    })
    btnSearchGame.disableProperty.bind(textSearchGameKey.textProperty.isEmpty)
  }

  def load(): Unit = load("")

  def load(title: String): Unit = {
    textSearchGameKey.setText(title)
    resetEvent()
  }

  private def resetEvent() = onLoadProperty.set(false)

  @FXML private def btnSearchGame_OnAction(actionEvent: ActionEvent) = {
    key = textSearchGameKey.getText.trim
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
      textSearchGameKey.setText(title)
    }
  }
}