package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.regex
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByName(private[this] val name: String) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = GameQuery.tlp.query
      .where(regex("name", "^" + name))
      .list

    FXCollections.observableArrayList(list)
  }
}
