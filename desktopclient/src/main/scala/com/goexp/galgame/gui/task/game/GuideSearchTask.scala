package com.goexp.galgame.gui.task.game

import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.gui.db.mongo.Query.GuideQuery
import com.mongodb.client.model.Filters.regex
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

object GuideSearchTask {

  class ByName(private[this] val name: String) extends Task[ObservableList[CommonGame.Guide]] {
    override protected def call: ObservableList[CommonGame.Guide] = {
      val list = GuideQuery.tlp.query
        .where(regex("title", "^"+name))
        .list

      FXCollections.observableArrayList(list)
    }
  }


}
