package com.goexp.galgame.gui.task.game.search.sub

import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.gui.db.mongo.query.GuideQuery
import com.mongodb.client.model.Filters.regex
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GuideSearchTask(private val name: String) extends Task[ObservableList[GameGuide]] {
  override protected def call: ObservableList[GameGuide] = {
    val list = GuideQuery()
      .where(regex("title", s"^$name"))
      .list()

    FXCollections.observableArrayList(list)
  }
}
