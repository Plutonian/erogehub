package com.goexp.galgame.gui.db.mongo

import java.util._

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.{CVCreator, CommonBrandCreator, CommonGameCreator}
import com.goexp.galgame.common.model._
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.util.cache.AppCache
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.{exclude, include}
import com.mongodb.client.model.Sorts.descending
import org.bson.Document
import org.slf4j.LoggerFactory

object Query {

  object BrandQuery {
    private val logger = LoggerFactory.getLogger(BrandQuery.getClass)

    private val creator: ObjectCreator[Brand] = (doc: Document) => {
      logger.debug("<create> doc={}", doc)

      val parentCreator = new CommonBrandCreator(new Brand)
      val brand = parentCreator.create(doc).asInstanceOf[Brand]

      brand.isLike = BrandType.from(doc.getInteger("type"))
      brand

    }
    val tlp = new DBQueryTemplate.Builder[Brand](DB_NAME, "brand", creator).build

  }

  object CVQuery {
    val tlp = new DBQueryTemplate.Builder[CV](DB_NAME, "cv", CVCreator).build
  }

  object GameQuery {
    private val TABLE_NAME = "game"
    object SimpleGame extends ObjectCreator[Game] {
      final private val logger = LoggerFactory.getLogger(SimpleGame.getClass)

      override def create(doc: Document): Game = {

        val parentCreator = new CommonGameCreator(new Game)
        val g = parentCreator.create(doc).asInstanceOf[Game]
        val brandId = doc.getInteger("brandId")

        g.brand = Option(AppCache.brandCache.get(brandId))
          .getOrElse {
            val brand = BrandQuery.tlp.query.where(Filters.eq(brandId)).one
            AppCache.brandCache.put(brandId, brand)
            brand
          }


        g.setState(GameState.from(doc.getInteger("state")))
        g.star = doc.getInteger("star")
        logger.debug("{}", g)
        g
      }
    }

    val tlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).defaultSelect(exclude("gamechar", "simpleImg"))
      .defaultSort(descending("publishDate", "name")).build
    val imgTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).defaultSelect(include("simpleImg")).build
    val personTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).defaultSelect(include("gamechar")).build
  }

  object TagQuery {
    private val logger = LoggerFactory.getLogger(TagQuery.getClass)
    private val creator: ObjectCreator[TagType] = (doc: Document) => {
      val t = new TagType
      t.`type` = doc.getString("type")
      t.order = doc.getInteger("order")
      t.tags = doc.get("tags", classOf[List[String]])
      t
    }
    val tlp = new DBQueryTemplate.Builder[TagType](DB_NAME, "tag", creator).build
  }

}