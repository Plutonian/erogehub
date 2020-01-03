package com.goexp.galgame.gui.task.game.search.sub

import com.goexp.galgame.common.model.game.GameImg
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GameImgListTask(private[this] val gameId: Int) extends Task[ObservableList[GameImg]] {
  override protected def call: ObservableList[GameImg] = {
    GameQuery.imgTlp.where(Filters.eq(gameId)).one()
      .map {
        _.gameImgs
      }
      .map {
        FXCollections.observableArrayList(_)
      }
      .getOrElse(FXCollections.emptyObservableList[GameImg])

  }
}