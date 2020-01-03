package com.goexp.galgame.gui.task.brand.search

import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters.regex
import javafx.concurrent.Task

import scala.collection.mutable


class ByComp(private val name: String) extends Task[mutable.Buffer[Brand]] {
  override protected def call = {
    BrandQuery().where(regex("comp", name))
      .scalaList()
  }
}
