package com.goexp.galgame.gui.task.game.change

import com.goexp.galgame.gui.db.mongo.gamedb.StateDB
import javafx.concurrent.Task

class MultiBlockByBrand(private[this] val brandId: Int) extends Task[Unit] {

  override protected def call: Unit = {
    StateDB.blockAllGame(brandId)
  }
}
