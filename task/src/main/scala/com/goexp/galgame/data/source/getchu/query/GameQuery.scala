package com.goexp.galgame.data.source.getchu.query

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.mongodb.client.model.Projections.exclude
import com.typesafe.scalalogging.Logger
import org.bson.Document

object GameQuery {

  val TABLE_NAME = "game"

  val fullTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).build
  val fullTlpWithChar = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame)
    .defaultSelect(exclude("simpleImg"))
    .build
  val simpleTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame)
    .defaultSelect(exclude("gamechar"))
    .defaultSelect(exclude("simpleImg"))
    .build


  object SimpleGame extends ObjectCreator[Game] {
    private val logger = Logger(SimpleGame.getClass)

    override def create(doc: Document) = {
      logger.trace(s"<create> doc=${doc}")

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]

      g.brandId = Option(doc.getInteger("brandId")).map(_.toInt).getOrElse(0)
      g.group = doc.getString("group")
      g.state = Option(doc.getInteger("state")).map(s => GameState.from(s)).getOrElse(GameState.UNCHECKED)

      logger.trace(s"<game>${g}")

      g
    }
  }

}