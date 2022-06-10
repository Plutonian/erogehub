package com.goexp.galgame.data.source.getchu.query

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.game.{GameLocation, PlayState}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.query.GameQuery.{SimpleGameCreator, TABLE_NAME}
import com.mongodb.client.model.Projections.exclude
import com.mongodb.client.model.Sorts.descending
import com.typesafe.scalalogging.Logger
import org.bson.Document

object GameQuery {

  val TABLE_NAME = "game"

  //  private object BrandCache {
  //    private val brandCache = new SimpleCache[Int, Brand]
  //
  //    def apply() = brandCache
  //
  //    def get(brandId: Int) = {
  //      brandCache.get(brandId).getOrElse {
  //        val brand = BrandQuery().where(Filters.eq(brandId)).one().orNull
  //        BrandCache().put(brandId, brand)
  //        brand
  //      }
  //    }
  //  }

  object SimpleGameCreator extends ObjectCreator[Game] {
    private val logger = Logger(SimpleGameCreator.getClass)

    override def create(doc: Document) = {
      logger.trace(s"<create> doc=${doc}")

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]

      val brandId = Option(doc.getInteger("brandId")).map(_.toInt).getOrElse(0)

      g.brandId = brandId
      //      g.group = doc.getString("group")
      //      g.state = Option(doc.getInteger("state")).map(GameState.from(_)).getOrElse(GameState.UNCHECKED)
      g.playState = Option(doc.getInteger("playState")).map(PlayState.from(_)).getOrElse(PlayState.NOT_PLAY)
      g.emotion = Option(doc.getInteger("emotion")).map(Emotion.from(_)).getOrElse(Emotion.NORMAL)

      g.location = GameLocation.from(doc.getInteger("location", GameLocation.REMOTE.value))
      //      g.brand = BrandCache.get(brandId)

      g.isSame = doc.getBoolean("isSame")
      g.star = doc.getInteger("star", 0)

      logger.trace(s"<game>${g}")

      g
    }
  }

}

object GameFullQuery {
  val fullTlp = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGameCreator)
    .defaultSort(descending("publishDate", "name"))
    .build

  def apply() = fullTlp
}

object GameFullWithCharQuery {
  val fullTlpWithChar = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGameCreator)
    .defaultSelect(exclude("simpleImg"))
    .defaultSort(descending("publishDate", "name"))
    .build

  def apply() = fullTlpWithChar
}

object GameSimpleQuery {
  val simpleTlp = DBQuery[Game](Config.DB_STRING, DB_NAME, TABLE_NAME, SimpleGameCreator)
    .defaultSelect(exclude("gamechar"))
    .defaultSelect(exclude("simpleImg"))
    .defaultSort(descending("publishDate", "name"))
    .build


  def apply() = simpleTlp
}