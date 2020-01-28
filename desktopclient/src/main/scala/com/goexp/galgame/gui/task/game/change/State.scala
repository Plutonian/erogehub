package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.StateDB
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task

class State(private val game: Game) extends Task[Void] {
  override protected def call: Void = {
    StateDB.update(game)
    null
  }
}
