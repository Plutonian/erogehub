package com.goexp.galgame.gui.task.game

import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.gui.db.mongo.Query.GameQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

class GameCharListTask(private[this] val gameId: Int) extends Task[ObservableList[GameCharacter]] {
  override protected def call: ObservableList[GameCharacter] = {
    val g = GameQuery.personTlp.query.where(Filters.eq(gameId)).one


    Option(g.gameCharacters)
      .map {
        FXCollections.observableArrayList(_)
      }
      .getOrElse(FXCollections.emptyObservableList[GameCharacter])
  }
}