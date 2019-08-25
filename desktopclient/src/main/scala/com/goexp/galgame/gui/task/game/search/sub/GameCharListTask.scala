package com.goexp.galgame.gui.task.game.search.sub

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.gui.db.mongo.query.GameQuery
import com.mongodb.client.model.Filters
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

import scala.jdk.CollectionConverters._

class GameCharListTask(private[this] val gameId: Int) extends Task[ObservableList[GameCharacter]] {
  override protected def call: ObservableList[GameCharacter] = {
    val g = GameQuery.personTlp.query.where(Filters.eq(gameId)).one

    Option(g.gameCharacters)
      .map(persons => {
        persons.asScala.to(LazyList)
          .groupBy(p => {
            if (Strings.isEmpty(p.cv)) {
              if (Strings.isEmpty(p.img)) 3 else 2
            } else 1
          }).to(LazyList)
          .sortBy({ case (k, _) => k })
          .flatMap({ case (_, v) => v }).asJava
      })
      .map(FXCollections.observableArrayList(_))
      .getOrElse(FXCollections.emptyObservableList[GameCharacter])

  }
}