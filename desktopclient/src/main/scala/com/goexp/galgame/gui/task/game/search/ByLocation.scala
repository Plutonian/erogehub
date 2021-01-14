package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByLocation(private val location: GameLocation) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {

    if (!(location eq GameLocation.REMOTE)) {
      val list = GameQuery()
        .where(Filters.eq("location", location.value))
        .list()

      FXCollections.observableArrayList(list)
    } else {
      val list = GameQuery()
        .where(and(Filters.exists("location", false), Filters.gte("state", 0)))
        .list()


      FXCollections.observableArrayList(list)
    }

  }
}
