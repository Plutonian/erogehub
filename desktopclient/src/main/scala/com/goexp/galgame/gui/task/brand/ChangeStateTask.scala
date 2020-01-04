package com.goexp.galgame.gui.task.brand

import com.goexp.galgame.gui.db.mongo.BrandDB
import com.goexp.galgame.gui.model.Brand
import javafx.concurrent.Task

class ChangeStateTask(private val brand: Brand) extends Task[Boolean] {
  override protected def call: Boolean = {
    BrandDB.updateIsLike(brand)
    true
  }
}