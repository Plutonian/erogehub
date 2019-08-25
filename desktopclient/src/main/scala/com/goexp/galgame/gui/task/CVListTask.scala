package com.goexp.galgame.gui.task

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.db.mongo.query.CVQuery
import com.mongodb.client.model.Sorts
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class CVListTask extends Task[ObservableList[CV]] {
  override protected def call: ObservableList[CV] = {
    val list = CVQuery.tlp.query.sort(Sorts.descending("star")).list
    FXCollections.observableArrayList(list)
  }
}