package com.goexp.galgame.gui.view.game.search

import com.goexp.galgame.gui.task.game.search.{ByName, ByNameEx}
import com.goexp.galgame.gui.view.game.ExplorerData
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, ToggleGroup}
import javafx.scene.layout.BorderPane

class SearchController extends DefaultController {
  @FXML private var textSearchGameKey: TextField = _
  @FXML private var searchGroup: ToggleGroup = _
  @FXML private var btnSearchGame: Button = _
  @FXML private var searchPanel: BorderPane = _

  private object VO {
    lazy val key = new SimpleStringProperty()
  }

  import VO._


  override protected def dataBinding(): Unit = {
    btnSearchGame.disableProperty.bind(textSearchGameKey.textProperty.isEmpty)
    textSearchGameKey.textProperty().bindBidirectional(key)
  }

  override protected def eventBinding(): Unit = {
    btnSearchGame.setOnAction(_ => {

      val searchType = SearchType.from(searchGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt)
      val task = searchType match {
        case SearchType.Simple => new ByName(key.get())
        case SearchType.Extend => new ByNameEx(key.get())
        //            case SearchType.Full => new ByTag(key)
        case _ => null
      }

      val conn = ExplorerData(task)
      searchPanel.setCenter(conn)
      conn.load()
    })
  }

  def load(title: String = ""): Unit = {
    key.set(title)
  }

}