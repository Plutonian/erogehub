package com.goexp.galgame.data.script.source.getchu.local.cal

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.source.getchu.importor.CVDB
import com.goexp.galgame.data.source.getchu.query.{CVQuery, GameSimpleQuery}
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

object CalCVGameTask {
  private val logger = Logger(CalCVGameTask.getClass)

  def main(args: Array[String]): Unit = {

    val cvList = CVQuery().scalaList().to(LazyList)
    logger.info("Init OK")

    cvList.foreach {
      cv =>

        val games = GameSimpleQuery()
          .where(
            Filters.and(
              Filters.eq("gamechar.truecv", cv.name),
              Filters.ne("state", GameState.SAME.value)
            )
          )
          .scalaList().to(LazyList)

        val statistics = GameStat.calStat(games)

        logger.trace(s"$statistics")

        CVDB.updateStatistics(cv, statistics)
    }
  }
}