package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByState(private[this] val gameState: GameState) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = GameQuery()
      .where(Filters.eq("state", gameState.value))
      .list()

    FXCollections.observableArrayList(list)
  }
}
