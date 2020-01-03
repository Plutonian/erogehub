package com.goexp.galgame.gui.task.game.change

import java.util

import com.goexp.galgame.gui.db.mongo.gamedb.StateDB
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task

class MultiLike(private val games: util.List[Game]) extends Task[Unit] {

  override protected def call: Unit = {
    StateDB.batchUpdate(games)
  }
}
