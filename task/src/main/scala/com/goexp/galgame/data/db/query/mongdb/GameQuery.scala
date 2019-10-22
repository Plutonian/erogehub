package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Projections.exclude
import org.bson.Document
import org.slf4j.LoggerFactory

object GameQuery {

  val fullTlp = new DBQueryTemplate.Builder[Game](DB_NAME, "game", SimpleGame).build
  val fullTlpWithChar = new DBQueryTemplate.Builder[Game](DB_NAME, "game", SimpleGame)
    .defaultSelect(exclude("simpleImg"))
    .build
  val simpleTlp = new DBQueryTemplate.Builder[Game](DB_NAME, "game", SimpleGame)
    .defaultSelect(exclude("gamechar"))
    .defaultSelect(exclude("simpleImg"))
    .build


  object SimpleGame extends ObjectCreator[Game] {
    private lazy val logger = LoggerFactory.getLogger(SimpleGame.getClass)

    override def create(doc: Document) = {

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]

      g.brandId = Option(doc.getInteger("brandId")).map(_.toInt).getOrElse(0)
      g.state = Option(doc.getInteger("state")).map(s => GameState.from(s)).getOrElse(GameState.UNCHECKED)
      logger.debug("{}", g)
      g
    }
  }

}