package com.goexp.galgame.data.task.local

import com.goexp.galgame.data.db.importor.mongdb.CVDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object CalCVGameTask {
  private val logger = LoggerFactory.getLogger(CalCVGameTask.getClass)


  def main(args: Array[String]) = {

    logger.info("Init OK")

    CVQuery.tlp.query
      .list.asScala.to(LazyList)
      .foreach(cv => {
        val games = GameQuery.simpleTlp.query
          .where(
            Filters.and(
              Filters.eq("gamechar.truecv", cv.name) //,
              //              Filters.ne("state", GameState.SAME.value),
              //              Filters.ne("state", GameState.BLOCK.value)
            )
          )
          .list

        val start = games.asScala.to(LazyList)
          .filter(_.publishDate != null)
          .map(_.publishDate)
          .minOption.orNull

        val end = games.asScala.to(LazyList)
          .filter(_.publishDate != null)
          .map(_.publishDate)
          .maxOption.orNull

        val count = games.size()

        CVDB.updateStatistics(cv, start, end, count)


      })
  }
}