package com.goexp.galgame.gui.db.mongo.query

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.descending
import org.bson.Document
import org.slf4j.LoggerFactory

object GameQuery {
  private val TABLE_NAME = "game"

  object SimpleGame extends ObjectCreator[Game] {
    final private val logger = LoggerFactory.getLogger(SimpleGame.getClass)

    override def create(doc: Document): Game = {
      logger.debug("Doc={}", doc)

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]
      val brandId = doc.getInteger("brandId")

      g.brand = Option(AppCache.brandCache.get(brandId))
        .getOrElse {
          val brand = BrandQuery.tlp.where(Filters.eq(brandId)).one()
          AppCache.brandCache.put(brandId, brand)
          brand
        }


      g.setState(GameState.from(doc.getInteger("state")))
      g.star = doc.getInteger("star")
      logger.debug("Game={}", g)
      g
    }
  }

  val tlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame)
    //      .defaultSelect(exclude("gamechar", "simpleImg"))
    .defaultSort(descending("publishDate", "name")).build
  val imgTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).defaultSelect(include("simpleImg")).build
  val personTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).defaultSelect(include("gamechar")).build
}
