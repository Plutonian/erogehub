package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.LocationDB
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task

class Location(private val game: Game) extends Task[Void] {
  override protected def call: Void = {
    LocationDB.update(game)
    null
  }
}
