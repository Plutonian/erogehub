package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.StarDB
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task


class Star(private val game: Game) extends Task[Unit] {
  override protected def call: Unit = {
    StarDB.update(game)
  }
}