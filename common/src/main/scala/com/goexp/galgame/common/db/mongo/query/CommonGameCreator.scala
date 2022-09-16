package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.game.{CommonGame, GameCharacter, GameImg}
import com.typesafe.scalalogging.Logger
import org.bson.Document

import java.util
import scala.jdk.CollectionConverters._


class CommonGameCreator(
                         private val game: CommonGame
                       ) extends ObjectCreator[CommonGame] {

  private val logger = Logger(classOf[CommonGameCreator])

  override def create(doc: Document): CommonGame = {

    game.id = doc.getInteger("_id")
    game.name = doc.getString("name")
    game.mainTitle = doc.getString("mainTitle")
    game.subTitle = doc.getString("subTitle")
    game.publishDate = Option(doc.getDate("publishDate")).map(DateUtil.toLocalDate).orNull
    game.intro = doc.getString("intro").replace(" ", "")
    game.story = doc.getString("story").replace(" ", "")
    game.smallImg = doc.getString("smallImg")
    game.painter = doc.get("painter", classOf[util.List[String]])
    game.writer = doc.get("writer", classOf[util.List[String]])


    val tags = doc.get("tag", classOf[util.List[String]])
    game.tag =
      if (tags != null)
        tags.asScala.to(LazyList).filter(Strings.isNotEmpty).asJava
      else List[String]().asJava


    game.`type` = doc.get("type", classOf[util.List[String]])

    import Creator._

    game.gameCharacters =
      Option(doc.get("gamechar").asInstanceOf[util.List[Document]])
        .map {
          _.asScala.to(LazyList).map(personCreator.create)
            .groupBy { p =>
              if (Strings.isEmpty(p.cv)) {
                if (Strings.isEmpty(p.img)) 3 else 2
              } else 1
            }.to(LazyList)
            .sortBy { case (k, _) => k }
            .flatMap { case (_, v) => v }.asJava
        }
        .getOrElse(util.List.of[GameCharacter]())


    game.gameImgs =
      Option(doc.get("simpleImg").asInstanceOf[util.List[Document]])
        .map {
          _.asScala.to(LazyList).map(imgCreator.create).asJava
        }
        .getOrElse(util.List.of[GameImg]())

    logger.trace(s"${game}")
    game
  }


  private[CommonGameCreator] object Creator {

    val personCreator: ObjectCreator[GameCharacter] = (doc: Document) => {
      val person = GameCharacter(
        name = doc.getString("name"),
        cv = doc.getString("cv"),
        intro = doc.getString("intro"),
        trueCV = doc.getString("truecv"),
        img = doc.getString("img"),
        index = doc.getInteger("index")
      )

      person.man = doc.getBoolean("man")

      logger.trace(s"${person}")

      person
    }

    val imgCreator: ObjectCreator[GameImg] = (doc: Document) => {
      val gameImg = GameImg(
        src = doc.getString("src"),
        index = doc.getInteger("index")
      )


      logger.trace(s"${gameImg}")

      gameImg
    }
  }

}
