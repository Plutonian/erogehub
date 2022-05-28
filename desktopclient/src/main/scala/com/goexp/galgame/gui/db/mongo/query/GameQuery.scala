package com.goexp.galgame.gui.db.mongo.query

import com.goexp.common.cache.SimpleCache
import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.db.mongo.query.GameQuery.SimpleGame
import com.goexp.galgame.gui.model.{Brand, Game}
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.descending
import com.typesafe.scalalogging.Logger
import org.bson.Document

object GameQuery {
  val TABLE_NAME = "game"

  private object BrandCache {
    private val brandCache = new SimpleCache[Int, Brand]

    def apply() = brandCache

    def get(brandId: Int) = {
      brandCache.get(brandId).getOrElse {
        val brand = BrandQuery().where(Filters.eq(brandId)).one().orNull
        BrandCache().put(brandId, brand)
        brand
      }
    }
  }

  object SimpleGame extends ObjectCreator[Game] {
    final private val logger = Logger(SimpleGame.getClass)

    override def create(doc: Document): Game = {
      logger.trace(s"<Doc> $doc")

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]
      val brandId = doc.getInteger("brandId")

      g.brand = BrandCache.get(brandId)


      g.state.set(GameState.from(doc.getInteger("state", GameState.UNCHECKED.value)))
      g.location.set(GameLocation.from(doc.getInteger("location", GameLocation.REMOTE.value)))
      g.star.set(doc.getInteger("star", 0))

      logger.trace(s"Game=${g}")

      g
    }
  }

  private val tpl = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGame)
    //      .defaultSelect(exclude("gamechar", "simpleImg"))
    .defaultSort(descending("publishDate", "name"))
    .build

  def apply() = tpl

}

object GameImgQuery {

  import GameQuery.TABLE_NAME

  private val tpl = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGame)
    .defaultSelect(include("simpleImg"))
    .build

  def apply() = tpl
}

object GamePersonQuery {

  import GameQuery.TABLE_NAME

  private val tpl = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGame)
    .defaultSelect(include("gamechar"))
    .build

  def apply() = tpl
}
