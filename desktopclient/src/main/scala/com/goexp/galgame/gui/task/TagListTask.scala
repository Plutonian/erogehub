package com.goexp.galgame.gui.task

import com.goexp.galgame.common.model.TagType
import com.goexp.galgame.gui.db.mongo.query.TagQuery
import com.mongodb.client.model.Sorts.ascending
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class TagListTask extends Task[ObservableList[TagType]] {
  override protected def call: ObservableList[TagType] =
    FXCollections.observableArrayList(TagQuery.tlp.query.sort(ascending("order")).list)
}