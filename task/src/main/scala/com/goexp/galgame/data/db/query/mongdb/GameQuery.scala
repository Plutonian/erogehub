package com.goexp.galgame.data.db.query.mongdb

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.common.util.DateUtil
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.common.model.{CommonGame, GameState}
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Projections.exclude
import org.bson.Document
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object GameQuery {

  type Person = GameCharacter
  lazy val fullTlp = new DBQueryTemplate.Builder[Game](DB_NAME, "game", new Creator.FullGame).build
  lazy val fullTlpWithChar = new DBQueryTemplate.Builder[Game](DB_NAME, "game", new Creator.FullGame)
    .defaultSelect(exclude("simpleImg"))
    .build

  object Creator {

    class GameChar extends ObjectCreator[Person] {
      private lazy val logger = LoggerFactory.getLogger(classOf[GameChar])

      override def create(doc: Document) = {
        logger.debug("{}", doc)

        val gameCharacter = new Person
        gameCharacter.name = doc.getString("name")
        gameCharacter.cv = doc.getString("cv")
        gameCharacter.intro = doc.getString("intro")
        gameCharacter.trueCV = doc.getString("truecv")
        gameCharacter.img = doc.getString("img")
        gameCharacter.index = doc.getInteger("index")

        gameCharacter
      }
    }

    class SimpleImg extends ObjectCreator[CommonGame.GameImg] {
      private lazy val logger = LoggerFactory.getLogger(classOf[SimpleImg])

      override def create(doc: Document): CommonGame.GameImg = {
        val gameImg = new CommonGame.GameImg
        gameImg.src = doc.getString("src")
        gameImg.index = doc.getInteger("index")
        logger.debug("{}", gameImg)
        gameImg
      }
    }

    class SimpleGame extends ObjectCreator[Game] {
      private lazy val logger = LoggerFactory.getLogger(classOf[SimpleGame])

      override def create(doc: Document) = {
        val g = new Game
        g.id = doc.getInteger("_id")
        g.name = doc.getString("name")
        g.brandId = Option(doc.getInteger("brandId")).map(_.toInt).getOrElse(0)

        Option(doc.getDate("publishDate")) match {
          case Some(date) => g.publishDate = DateUtil.toLocalDate(date)
          case _ =>
        }

        g.intro = doc.getString("intro")
        g.story = doc.getString("story")
        g.smallImg = doc.getString("smallImg")
        g.painter = doc.get("painter").asInstanceOf[util.List[String]]
        g.writer = doc.get("writer").asInstanceOf[util.List[String]]
        g.tag = doc.get("tag").asInstanceOf[util.List[String]]
        g.`type` = doc.get("type").asInstanceOf[util.List[String]]

        g.state = Option(doc.getInteger("state")).map(s => GameState.from(s)).getOrElse(GameState.UNCHECKED)
        logger.debug("{}", g)
        g
      }
    }

    class FullGame extends Creator.SimpleGame {
      lazy val gamecharCreator = new Creator.GameChar
      lazy val simpleImgCreator = new Creator.SimpleImg

      override def create(doc: Document) = {
        val g = super.create(doc)
        g.gameCharacters =
          Option(doc.get("gamechar"))
            .map(list => {
              list.asInstanceOf[util.List[Document]]
                .asScala
                .toStream
                .map(gamecharCreator.create)
                .toList
            }).getOrElse(List.empty).asJava

        g.gameImgs =
          Option(doc.get("simpleImg"))
            .map(list => {
              list.asInstanceOf[util.List[Document]]
                .asScala
                .toStream
                .map(simpleImgCreator.create)
                .toList

            }).getOrElse(List.empty).asJava
        g
      }
    }

  }

}