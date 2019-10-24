package com.goexp.galgame.gui.task.game.search

import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task


class ByBrand(private[this] val brandId: Int) extends Task[ObservableList[Game]] {
  override protected def call: ObservableList[Game] = {

    val list = GameQuery.tlp.where(Filters.eq("brandId", brandId)).list()

    FXCollections.observableArrayList(list)
  }
}