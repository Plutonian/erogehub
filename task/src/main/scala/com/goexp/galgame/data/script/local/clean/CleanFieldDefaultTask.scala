package com.goexp.galgame.data.script.local.clean

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GameQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters.*

object CleanFieldDefaultTask {
  val tpl = new DBOperator(Config.DB_STRING, DB_NAME, "game")
  private val logger = Logger(CleanFieldDefaultTask.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("Start cleaning fields...")

    // 清洗 location 字段
    cleanLocation()

    // 清洗 emotion 字段
    cleanEmotion()

    // 清洗 playState 字段
    cleanPlayState()


    cleanStar()

    logger.info("Clean completed!")
  }

  private def cleanLocation(): Unit = {
    logger.info("Start cleaning location field...")

    val games = GameQuery()
      .find()
      .filter(Filters.or(
        Filters.exists("location", false),
        Filters.eq("location", null)
      ))
      .asScala
      .to(LazyList)

    logger.info(s"Games to update location: ${games.size}")

    var count = 0
    games.foreach { game =>
      count += 1
      val id = game.getInteger("_id")

      tpl.exec { documentMongoCollection =>
        documentMongoCollection.updateOne(
          Filters.eq("_id", id),
          Updates.set("location", 0)
        )
      }

      logger.info(s"[${count}] Game ID: ${id}, set location = 0")
    }

    logger.info(s"Location clean completed! Total updated: ${count}")
  }

  private def cleanEmotion(): Unit = {
    logger.info("Start cleaning emotion field...")

    val games = GameQuery()
      .find()
      .filter(Filters.or(
        Filters.exists("emotion", false),
        Filters.eq("emotion", null)
      ))
      .asScala
      .to(LazyList)

    logger.info(s"Games to update emotion: ${games.size}")

    var count = 0
    games.foreach { game =>
      count += 1
      val id = game.getInteger("_id")

      tpl.exec { documentMongoCollection =>
        documentMongoCollection.updateOne(
          Filters.eq("_id", id),
          Updates.set("emotion", 0)
        )
      }

      logger.info(s"[${count}] Game ID: ${id}, set emotion = 0")
    }

    logger.info(s"Emotion clean completed! Total updated: ${count}")
  }

  private def cleanPlayState(): Unit = {
    logger.info("Start cleaning playState field...")

    val games = GameQuery()
      .find()
      .filter(Filters.or(
        Filters.exists("playState", false),
        Filters.eq("playState", null)
      ))
      .asScala
      .to(LazyList)

    logger.info(s"Games to update playState: ${games.size}")

    var count = 0
    games.foreach { game =>
      count += 1
      val id = game.getInteger("_id")

      tpl.exec { documentMongoCollection =>
        documentMongoCollection.updateOne(
          Filters.eq("_id", id),
          Updates.set("playState", 0)
        )
      }

      logger.info(s"[${count}] Game ID: ${id}, set playState = 0")
    }

    logger.info(s"PlayState clean completed! Total updated: ${count}")
  }

  private def cleanStar(): Unit = {
    logger.info("Start cleaning playState field...")

    val games = GameQuery()
      .find()
      .filter(Filters.or(
        Filters.exists("star", false),
        Filters.eq("star", null)
      ))
      .asScala
      .to(LazyList)

    logger.info(s"Games to update star: ${games.size}")

    var count = 0
    games.foreach { game =>
      count += 1
      val id = game.getInteger("_id")

      tpl.exec { documentMongoCollection =>
        documentMongoCollection.updateOne(
          Filters.eq("_id", id),
          Updates.set("star", 0)
        )
      }

      logger.info(s"[${count}] Game ID: ${id}, set star = 0")
    }

    logger.info(s"star clean completed! Total updated: ${count}")
  }
}
