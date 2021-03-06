package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{and, gte, lte}
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByStarRange(private val begin: Int,
                  private val end: Int) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = GameQuery()
      .where(
        and(
          //          Filters.eq("state", GameState.PLAYED.value),
          gte("star", begin),
          lte("star", end)
        )
      )
      .list()

    FXCollections.observableArrayList(list)
  }
}
