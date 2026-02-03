package com.goexp.galgame.data.script.local.clean

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.data.source.getchu.query.GameSimpleQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import com.typesafe.scalalogging.Logger

object CleanGameTitleTask {
  val tpl = new DBOperator(Config.DB_STRING, DB_NAME, "game")
  private val logger = Logger(CleanGameTitleTask.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("Start cleaning game titles...")
    var count = 0

    val games = GameSimpleQuery()
      .where(Filters.and(
        Filters.exists("name", true),
        Filters.exists("mainTitle", false)
      ))
      .scalaList()
      .to(LazyList)

    logger.info(s"Games to update: ${games.size}")

    games.foreach { game =>
      count += 1
      val titles = game.titles

      tpl.exec { documentMongoCollection =>
        documentMongoCollection.updateOne(
          Filters.eq(game.id),
          combine(
            set("mainTitle", Option(titles.mainTitle).map(_.trim).orNull),
            set("subTitle", Option(titles.subTitle).map(_.trim).orNull)
          )
        )
      }

      logger.info(s"[${count}] Game ID: ${game.id}")
      logger.info(s"  New: ${titles.mainTitle} ${titles.subTitle}")
    }

    logger.info(s"Clean completed! Total updated: ${count}")
  }
}
