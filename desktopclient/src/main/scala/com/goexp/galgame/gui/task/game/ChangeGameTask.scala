package com.goexp.galgame.gui.task.game

import java.util

import com.goexp.galgame.gui.db.mongo.GameDB.{StarDB, StateDB}
import com.goexp.galgame.gui.model.Game
import javafx.concurrent.Task

object ChangeGameTask {

  class Like(game: Game) extends Task[Void] {
    override protected def call: Void = {
      StateDB.update(game)
      null
    }
  }

  class MultiLike(games: util.List[Game]) extends Task[Void] {

    override protected def call: Void = {
      StateDB.batchUpdate(games)
      null
    }
  }

  class MultiLikeByBrand(brandId: Int) extends Task[Void] {

    override protected def call: Void = {
      StateDB.update(brandId)
      null
    }
  }

  class Star(game: Game) extends Task[Void] {
    override protected def call: Void = {
      StarDB.update(game)
      null
    }
  }

}