package com.goexp.galgame.gui.db.mongo

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.common.util.DateUtil
import com.goexp.galgame.common.model._
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

    val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, new Creator.FullGame).defaultSelect(exclude("gamechar", "simpleImg")).defaultSort(descending("publishDate", "name")).build

    private[mongo] object Creator {

      private val logger = LoggerFactory.getLogger(GameQuery.getClass)

      private val personCreator: ObjectCreator[CommonGame.GameCharacter] = (doc: Document) => {
        val gameCharacter = new CommonGame.GameCharacter
        gameCharacter.name = doc.getString("name")
        gameCharacter.cv = doc.getString("cv")
        gameCharacter.intro = doc.getString("intro")
        gameCharacter.trueCV = doc.getString("truecv")
        gameCharacter.img = doc.getString("img")
        gameCharacter.index = doc.getInteger("index")
        logger.debug("{}", gameCharacter)
        gameCharacter
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

          //          Optional.ofNullable(doc.getDate("publishDate")).ifPresent((date: Date) => g.publishDate = DateUtil.toLocalDate(date))
          g.intro = doc.getString("intro")
          g.story = doc.getString("story")
          g.smallImg = doc.getString("smallImg")
          g.painter = doc.get("painter").asInstanceOf[util.List[String]]
          g.writer = doc.get("writer").asInstanceOf[util.List[String]]
          g.tag = doc.get("tag").asInstanceOf[util.List[String]]
          g.`type` = doc.get("type").asInstanceOf[util.List[String]]
          g.setState(GameState.from(doc.getInteger("state")))
          g.star = doc.getInteger("star")
          logger.debug("{}", g)
          g
        }
      }

      private[mongo] class FullGame extends Creator.SimpleGame {

        override def create(doc: Document): Game = {
          val g = super.create(doc)

          g.gameCharacters =
            Option(doc.get("gamechar").asInstanceOf[util.List[Document]])
              .map({
                _.asScala.toStream.map(p => personCreator.create(p)).asJava
              })
              .getOrElse(util.List.of[CommonGame.GameCharacter]())


          g.gameImgs =
            Option(doc.get("simpleImg").asInstanceOf[util.List[Document]])
              .map({
                _.asScala.toStream.map(p => imgCreator.create(p)).asJava
              })
              .getOrElse(util.List.of[CommonGame.GameImg]())

          g
        }
      }

    }

    object GameCharQuery {
      val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, new Creator.FullGame).defaultSelect(include("gamechar")).build
    }

    object GameImgQuery {
      val tlp = new DBQueryTemplate.Builder[Game](DB, TABLE_NAME, new Creator.FullGame).defaultSelect(include("simpleImg")).build
    }

  }

  object TagQuery {
    private val logger = LoggerFactory.getLogger(TagQuery.getClass)

    private val creator: ObjectCreator[TagType] = (doc: Document) => {
      val t = new TagType
      t.`type` = doc.getString("type")
      t.order = doc.getInteger("order")
      t.tags = doc.get("tags", classOf[util.List[String]])
      t
    }

    val tlp = new DBQueryTemplate.Builder[TagType](DB, "tag", creator).build
  }

}