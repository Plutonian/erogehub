package com.goexp.galgame.gui.task.game.search

import java.time.LocalDate

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{and, gte, lte}
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class ByDateRange(private[this] val start: LocalDate, private[this] val end: LocalDate) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {
    val list = GameQuery.tlp.query
      .where(and(
        gte("publishDate", DateUtil.toDate(start.toString + " 00:00:00")),
        lte("publishDate", DateUtil.toDate(end.toString + " 23:59:59"))))
      .list


    FXCollections.observableArrayList(list)
  }
}
