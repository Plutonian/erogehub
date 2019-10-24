package com.goexp.galgame.gui.task.game.search.sub

import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.gui.db.mongo.query.GuideQuery
import com.mongodb.client.model.Filters.regex
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GuideSearchTask(private[this] val name: String) extends Task[ObservableList[CommonGame.Guide]] {
  override protected def call: ObservableList[CommonGame.Guide] = {
    val list = GuideQuery.tlp
      .where(regex("title", "^" + name))
      .list()

    FXCollections.observableArrayList(list)
  }
}
