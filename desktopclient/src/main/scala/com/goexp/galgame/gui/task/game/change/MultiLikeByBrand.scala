package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.StateDB
import javafx.concurrent.Task

class MultiLikeByBrand(private[this] val brandId: Int) extends Task[Void] {

  override protected def call: Void = {
    StateDB.update(brandId)
    null
  }
}
