package com.goexp.galgame.common.db.mongo.query

import java.util.List

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.common.util.DateUtil
import com.goexp.galgame.common.model.CommonGame
import org.bson.Document
import org.slf4j.LoggerFactory

//import scala.jdk.CollectionConverters._
import scala.jdk.CollectionConverters._


class CommonGameCreator(private[this] val game: CommonGame) extends ObjectCreator[CommonGame] {
  private lazy val logger = LoggerFactory.getLogger(classOf[CommonGameCreator])

  override def create(doc: Document): CommonGame = {

    game.id = doc.getInteger("_id")
    game.name = doc.getString("name")
    game.publishDate = Option(doc.getDate("publishDate")).map(DateUtil.toLocalDate).orNull
    game.intro = doc.getString("intro")
    game.story = doc.getString("story")
    game.smallImg = doc.getString("smallImg")
    game.painter = doc.get("painter", classOf[List[String]])
    game.writer = doc.get("writer", classOf[List[String]])
    game.tag = doc.get("tag", classOf[List[String]])
    game.`type` = doc.get("type", classOf[List[String]])

    import Creator._

    game.gameCharacters =
      Option(doc.get("gamechar").asInstanceOf[List[Document]])
        .map({
          _.asScala.to(LazyList).map(p => personCreator.create(p)).asJava
        })
        .getOrElse(List.of[CommonGame.GameCharacter]())


    game.gameImgs =
      Option(doc.get("simpleImg").asInstanceOf[List[Document]])
        .map({
          _.asScala.to(LazyList).map(p => imgCreator.create(p)).asJava
        })
        .getOrElse(List.of[CommonGame.GameImg]())

    logger.debug("{}", game)
    game
  }


  private[CommonGameCreator] object Creator {

    val personCreator: ObjectCreator[CommonGame.GameCharacter] = (doc: Document) => {
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

    val imgCreator: ObjectCreator[CommonGame.GameImg] = (doc: Document) => {
      val gameImg = new CommonGame.GameImg
      gameImg.src = doc.getString("src")
      gameImg.index = doc.getInteger("index")
      logger.debug("{}", gameImg)
      gameImg
    }
  }

}
