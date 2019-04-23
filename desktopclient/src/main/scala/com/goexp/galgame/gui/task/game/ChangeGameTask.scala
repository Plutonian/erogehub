package com.goexp.galgame.gui.task.game

import java.util

import com.goexp.galgame.gui.db.mongo.GameDB.{StarDB, StateDB}
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task

object ChangeGameTask {

  class Like(private[this] val game: Game) extends Task[Void] {
    override protected def call: Void = {
      StateDB.update(game)
      null
    }
  }

  class MultiLike(private[this] val games: util.List[Game]) extends Task[Void] {

    override protected def call: Void = {
      StateDB.batchUpdate(games)
      null
    }
  }

  class MultiLikeByBrand(private[this] val brandId: Int) extends Task[Void] {

    override protected def call: Void = {
      StateDB.update(brandId)
      null
    }
  }

  class Star(private[this] val game: Game) extends Task[Void] {
    override protected def call: Void = {
      StarDB.update(game)
      null
    }
  }

}