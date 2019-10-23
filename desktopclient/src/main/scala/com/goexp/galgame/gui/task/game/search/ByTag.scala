package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByTag(private[this] val tag: String) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = GameQuery.tlp.query
      .where(Filters.or(
        Filters.eq("tag", tag),
        regex("name", tag)
      ))
      .list


    FXCollections.observableArrayList(list)
  }
}
