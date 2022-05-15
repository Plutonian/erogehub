package com.goexp.galgame.gui.view.game.explorer.sidebar

import java.util
import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.TaskService
import com.goexp.galgame.gui.task.game.panel.group.ByDate
import com.goexp.galgame.gui.task.game.panel.group.node.DateItem
import com.goexp.ui.javafx.control.cell.TextTreeCell
import javafx.fxml.FXML
import javafx.scene.control.TreeView
import scalafx.Includes._

import java.time.LocalDate

class DateGroupController extends FilterController[Game] {
  @FXML private var dateTree: TreeView[DateItem] = _
  private var filteredGames: util.List[Game] = _
  final private val groupDateServ = TaskService(new ByDate(filteredGames))

  var selectedDate: DateItem = _


  override protected def initComponent(): Unit = {
    dateTree.setCellFactory(_ =>
      TextTreeCell[DateItem] { item =>
        item.title
      }
    )
  }

  override protected def eventBinding(): Unit = {
    dateTree.getSelectionModel.selectedItem.onChange((_, _, newValue) => {
      if (newValue != null) {

        selectedDate = newValue.getValue

        onSetProperty.set(true)
        onSetProperty.set(false)
      }
    })
    groupDateServ.value.onChange((_, _, newValue) => {
      if (newValue != null) dateTree.setRoot(newValue)

    })

  }

  override def init(filteredGames: util.List[Game]) = {
    this.filteredGames = filteredGames
    groupDateServ.restart()
  }
}