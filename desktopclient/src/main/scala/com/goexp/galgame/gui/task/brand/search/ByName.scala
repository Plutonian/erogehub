package com.goexp.galgame.gui.task.brand.search

import java.util

import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters.regex
import javafx.concurrent.Task

class ByName(private val name: String) extends Task[util.List[Brand]] {
  override protected def call = {
    BrandQuery().where(regex("name", "^" + name))
      .list()
  }
}
