package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.StarDB
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task


class Star(private[this] val game: Game) extends Task[Void] {
  override protected def call: Void = {
    StarDB.update(game)
    null
  }
}