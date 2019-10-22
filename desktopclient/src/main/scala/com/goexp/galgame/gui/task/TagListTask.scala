package com.goexp.galgame.gui.task

import com.goexp.galgame.common.model.TagType
import com.goexp.galgame.gui.db.mongo.query.TagQuery
import com.mongodb.client.model.Sorts.ascending
import javafx.concurrent.Task

import scala.collection.mutable

class TagListTask extends Task[mutable.Buffer[TagType]] {
  override protected def call =
    TagQuery.tlp.query.sort(ascending("order")).scalaList
}