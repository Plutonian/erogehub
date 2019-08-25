package com.goexp.galgame.gui.task.game

import com.goexp.galgame.common.model.CommonGame.GameImg
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GameImgListTask(private[this] val gameId: Int) extends Task[ObservableList[GameImg]] {
  override protected def call: ObservableList[GameImg] = {
    val g = GameQuery.imgTlp.query.where(Filters.eq(gameId)).one


    Option(g.gameImgs)
      .map {
        FXCollections.observableArrayList(_)
      }
      .getOrElse(FXCollections.emptyObservableList[GameImg])

  }
}