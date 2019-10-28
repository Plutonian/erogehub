package com.goexp.galgame.gui.view.game.listview.sidebar

import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.panel.group.ByDate
import com.goexp.galgame.gui.task.game.panel.group.node.DateItem
import javafx.fxml.FXML
import javafx.scene.control.{TreeCell, TreeView}

class DateGroupController extends FilterController[Game] {
  @FXML private var dateTree: TreeView[DateItem] = _
  private var filteredGames: util.List[Game] = _
  final private val groupDateServ = TaskService(() => new ByDate(filteredGames))

  override protected def initialize() = {
    dateTree.setCellFactory((_: TreeView[DateItem]) => new TreeCell[DateItem]() {
      override protected def updateItem(item: DateItem, empty: Boolean) = {
        super.updateItem(item, empty)
        setGraphic(null)
        setText(null)
        if (!empty && item != null) setText(item.title)
      }
    })
    dateTree.getSelectionModel.selectedItemProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        predicate = (game: Game) => game.publishDate != null &&
          game.publishDate.isBefore(newValue.getValue.range.end) &&
          game.publishDate.isAfter(newValue.getValue.range.start)

        onSetProperty.set(true)
        onSetProperty.set(false)
      }
    })
    groupDateServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) dateTree.setRoot(newValue)

    })
  }

  override def init(filteredGames: util.List[Game]) = {
    this.filteredGames = filteredGames
    groupDateServ.restart()
  }
}