package com.goexp.galgame.gui.task.brand.list

import java.util

import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.mongodb.client.model.Sorts.descending
import javafx.concurrent.Task


class ByComp(private[this] val name: String) extends Task[util.List[Brand]] {
  override protected def call = {
    BrandQuery.tlp.query
      .where(and(
        Filters.eq("comp", name),
        Filters.ne("type", BrandType.BLOCK.value)
      ))
      .sort(and(descending("type"), descending("name")))
      .list
  }
}