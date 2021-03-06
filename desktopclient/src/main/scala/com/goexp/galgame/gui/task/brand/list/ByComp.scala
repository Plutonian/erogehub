package com.goexp.galgame.gui.task.brand.list

import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.mongodb.client.model.Sorts.descending
import javafx.concurrent.Task

import scala.collection.mutable


class ByComp(private val name: String) extends Task[mutable.Buffer[Brand]] {
  override protected def call = {
    BrandQuery().where(and(
      Filters.eq("comp", name),
      Filters.ne("type", BrandState.BLOCK.value)
    ))
      .sort(and(descending("type"), descending("name")))
      .scalaList()
  }
}
