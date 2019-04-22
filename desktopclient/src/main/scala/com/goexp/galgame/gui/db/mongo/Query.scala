package com.goexp.galgame.gui.db.mongo

import java.util._

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.common.util.DateUtil
import com.goexp.galgame.common.model._
import com.goexp.galgame.gui.db.mongo.Query.GameQuery.Creator.FullGame
import com.goexp.galgame.gui.model.{Brand, Game}
import com.mongodb.client.model.Projections.{exclude, include}
import com.mongodb.client.model.Sorts.descending
import org.bson.Document
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Query {
  private val DB = "galgame"

  object BrandQuery {

    private val logger = LoggerFactory.getLogger(BrandQuery.getClass)

    private val creator: ObjectCreator[Brand] = (doc: Document) => {
      val g = new Brand
      logger.debug("<create> doc={}", doc)
      g.id = doc.getInteger("_id")
      g.name = doc.getString("name")
      g.website = doc.getString("website")
      g.isLike = BrandType.from(doc.getInteger("type"))
      g.comp = doc.getString("comp")
      g
    }

    val tlp = new DBQueryTemplate.Builder[Brand](DB, "brand", creator).build

  }

  object CVQuery {

    private val logger = LoggerFactory.getLogger(CVQuery.getClass)

    private val creator: ObjectCreator[CV] = (doc: Document) => {
      val g = new CV
      logger.debug("<create> doc={}", doc)
      g.name = doc.getString("name")
      g.star = doc.getInteger("star")
      g.nameStr = doc.getString("names")
      g
    }

    val tlp = new DBQueryTemplate.Builder[CV](DB, "cv", creator).build

  }

  object GameQuery {
    private val TABLE_NAME = "game"

    val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, FullGame).defaultSelect(exclude("gamechar", "simpleImg")).defaultSort(descending("publishDate", "name")).build

    private[mongo] object Creator {

      private val logger = LoggerFactory.getLogger(GameQuery.getClass)

      private val personCreator: ObjectCreator[CommonGame.GameCharacter] = (doc: Document) => {
        val person = new CommonGame.GameCharacter
        person.name = doc.getString("name")
        person.cv = doc.getString("cv")
        person.intro = doc.getString("intro")
        person.trueCV = doc.getString("truecv")
        person.img = doc.getString("img")
        person.index = doc.getInteger("index")
        logger.debug("{}", person)
        person
      }

      private val imgCreator: ObjectCreator[CommonGame.GameImg] = (doc: Document) => {
        val gameImg = new CommonGame.GameImg
        gameImg.src = doc.getString("src")
        gameImg.index = doc.getInteger("index")
        logger.debug("{}", gameImg)
        gameImg
      }


      abstract private[mongo] class SimpleGame extends ObjectCreator[Game] {
        final private val logger = LoggerFactory.getLogger(classOf[Creator.SimpleGame])

        override def create(doc: Document): Game = {
          val g = new Game
          g.id = doc.getInteger("_id")
          g.name = doc.getString("name")
          g.brand = new Brand
          g.brand.id = doc.getInteger("brandId")
          g.publishDate = Option(doc.getDate("publishDate")).map(DateUtil.toLocalDate).orNull
          g.intro = doc.getString("intro")
          g.story = doc.getString("story")
          g.smallImg = doc.getString("smallImg")
          g.painter = doc.get("painter", classOf[List[String]])
          g.writer = doc.get("writer", classOf[List[String]])
          g.tag = doc.get("tag", classOf[List[String]])
          g.`type` = doc.get("type", classOf[List[String]])
          g.setState(GameState.from(doc.getInteger("state")))
          g.star = doc.getInteger("star")
          logger.debug("{}", g)
          g
        }
      }

      private[mongo] object FullGame extends Creator.SimpleGame {

        override def create(doc: Document): Game = {
          val g = super.create(doc)

          g.gameCharacters =
            Option(doc.get("gamechar").asInstanceOf[List[Document]])
              .map({
                _.asScala.toStream.map(p => personCreator.create(p)).asJava
              })
              .getOrElse(List.of[CommonGame.GameCharacter]())


          g.gameImgs =
            Option(doc.get("simpleImg").asInstanceOf[List[Document]])
              .map({
                _.asScala.toStream.map(p => imgCreator.create(p)).asJava
              })
              .getOrElse(List.of[CommonGame.GameImg]())

          g
        }
      }

    }

    object GameCharQuery {
      val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, FullGame).defaultSelect(include("gamechar")).build
    }

    object GameImgQuery {
      val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, FullGame).defaultSelect(include("simpleImg")).build
    }

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

    val tlp = new DBQueryTemplate.Builder[TagType](DB, "tag", creator).build
  }

}