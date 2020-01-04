package com.goexp.galgame.gui.task.brand.search

import java.util

import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import javafx.concurrent.Task

class ByType(private val state: BrandState) extends Task[util.List[Brand]] {
  override protected def call = {
    val query =
      if (state eq BrandState.ALL) {
        BrandQuery()
      }
      else {
        BrandQuery().where(Filters.eq("type", state.value))
      }

    query.list()
  }
}
