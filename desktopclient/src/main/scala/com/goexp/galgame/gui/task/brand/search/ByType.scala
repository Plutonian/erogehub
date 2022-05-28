package com.goexp.galgame.gui.task.brand.search

import com.goexp.galgame.common.model.brand.BrandState
import java.util
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import javafx.concurrent.Task

class ByType(private val state: BrandState) extends Task[util.List[Brand]] {
  override protected def call = {

    BrandQuery()
      .where({
        if (state eq BrandState.ALL)
          null
        else
          Filters.eq("type", state.value)
      })
      .list()
  }
}
