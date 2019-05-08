package com.goexp.galgame.gui.task.game

import com.goexp.common.util.Strings
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.gui.db.mongo.Query.GameQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

import scala.collection.JavaConverters._

class GameCharListTask(private[this] val gameId: Int) extends Task[ObservableList[GameCharacter]] {
  override protected def call: ObservableList[GameCharacter] = {
    val g = GameQuery.personTlp.query.where(Filters.eq(gameId)).one

    Option(g.gameCharacters)
      .map(persons => {
        persons.asScala.toStream
          .groupBy(p => {
            if (Strings.isEmpty(p.cv)) {
              if (Strings.isEmpty(p.img)) 3 else 2
            } else 1
          }).toStream
          .sortBy({ case (k, _) => k })
          .flatMap({ case (_, v) => v }).asJava
      })
      .map(FXCollections.observableArrayList(_))
      .getOrElse(FXCollections.emptyObservableList[GameCharacter])

  }
}