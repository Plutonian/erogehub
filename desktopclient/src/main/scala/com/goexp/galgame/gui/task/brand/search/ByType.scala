package com.goexp.galgame.gui.task.brand.search

import java.util

import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import javafx.concurrent.Task

class ByType(private[this] val `type`: BrandType) extends Task[util.List[Brand]] {
  override protected def call = {
    if (`type` eq BrandType.ALL)
      BrandQuery.tlp.query
        .list
    else
      BrandQuery.tlp.query
        .where(Filters.eq("type", `type`.value))
        .list

  }
}
