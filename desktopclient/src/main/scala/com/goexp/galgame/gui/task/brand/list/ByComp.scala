package com.goexp.galgame.gui.task.brand.list

import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.mongodb.client.model.Sorts.descending
import javafx.concurrent.Task

import scala.collection.mutable


class ByComp(private val name: String) extends Task[mutable.Buffer[Brand]] {
  override protected def call = {
    BrandQuery().where(
      Filters.eq("comp", name)
    )
      .sort(and(descending("type"), descending("name")))
      .scalaList()
  }
}
