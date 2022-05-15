package com.goexp.galgame.gui.view.game.search

import com.goexp.galgame.gui.task.game.search.{ByName, ByNameEx}
import com.goexp.galgame.gui.view.game.ExplorerData
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, ToggleGroup}
import javafx.scene.layout.BorderPane
import scalafx.Includes._

class SearchController extends DefaultController {
  @FXML private var textSearchGameKey: TextField = _
  @FXML private var searchGroup: ToggleGroup = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _

  lazy val title = new SimpleStringProperty()

  override protected def dataBinding(): Unit = {
    btnSearchGame.disable <== textSearchGameKey.text.isEmpty
    textSearchGameKey.text <==> title
  }

  override protected def eventBinding(): Unit = {
    btnSearchGame.setOnAction(_ => {

      val searchType = SearchType.from(searchGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt)
      val task = searchType match {
        case SearchType.Simple => new ByName(title.value)
        case SearchType.Extend => new ByNameEx(title.value)
        //            case SearchType.Full => new ByTag(key)
        case _ => null
      }

      val conn = ExplorerData(task)
      searchPanel.setCenter(conn)
      conn.load()
    })
  }

}