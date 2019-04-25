package com.goexp.galgame.gui.task.brand

import java.util

import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.db.mongo.Query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.mongodb.client.model.Sorts.descending
import javafx.concurrent.Task

object BrandListTask {

  class ByComp(private[this] val name: String) extends Task[util.List[Brand]] {
    override protected def call = {
      BrandQuery.tlp.query
        .where(and(
          Filters.eq("comp", name),
          Filters.ne("type", BrandType.PASS.getValue)
        ))
        .sort(and(descending("type"), descending("name")))
        .list
    }
  }

}