package com.goexp.galgame.data.script.source.getchu.local.cal

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.source.getchu.importor.CVDB
import com.goexp.galgame.data.source.getchu.query.{CVQuery, GameQuery}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

object CalCVGameTask {
  private val logger = LoggerFactory.getLogger(CalCVGameTask.getClass)

  def main(args: Array[String]): Unit = {

    val cvList = CVQuery.tlp.scalaList().to(LazyList)
    logger.info("Init OK")

    cvList.foreach {
      cv =>

        val games = GameQuery.simpleTlp
          .where(
            Filters.and(
              Filters.eq("gamechar.truecv", cv.name),
              Filters.ne("state", GameState.SAME.value)
            )
          )
          .scalaList().to(LazyList)


        val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
        val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

        val count = games.size


        logger.trace(s"$start, $end, $count")
        CVDB.updateStatistics(cv, start.orNull, end.orNull, count)
    }
  }
}