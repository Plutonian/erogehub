package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByCV(private[this] val cv: String,
           private[this] val real: Boolean) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = if (real)
      GameQuery.tlp.where(Filters.eq("gamechar.truecv", cv)).list()
    else
      GameQuery.tlp.where(Filters.eq("gamechar.cv", cv)).list()


    FXCollections.observableArrayList(list)
  }
}
