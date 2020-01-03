package com.goexp.galgame.gui.task.brand.search

import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import javafx.concurrent.Task

import scala.collection.mutable

class ByType(private[this] val `type`: BrandState) extends Task[mutable.Buffer[Brand]] {
  override protected def call = {
    if (`type` eq BrandState.ALL)
      BrandQuery().scalaList()
    else
      BrandQuery()
        .where(Filters.eq("type", `type`.value))
        .scalaList()

  }
}
