package com.goexp.galgame.gui.task.game.search.sub

import com.goexp.galgame.common.model.game.GameImg
import com.goexp.galgame.gui.db.mongo.query.GameImgQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GameImgListTask(private val gameId: Int) extends Task[ObservableList[GameImg]] {
  override protected def call: ObservableList[GameImg] = {
    GameImgQuery().where(Filters.eq(gameId)).one()
      .map {
        _.gameImgs
      }
      .map {
        FXCollections.observableArrayList(_)
      }
      .getOrElse(FXCollections.emptyObservableList[GameImg])

  }
}