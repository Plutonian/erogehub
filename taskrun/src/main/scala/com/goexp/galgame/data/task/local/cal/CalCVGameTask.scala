package com.goexp.galgame.data.task.local.cal

import java.time.LocalDate

import com.goexp.galgame.data.db.importor.mongdb.CVDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.goexp.galgame.data.task.ansyn.Pool._
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters._


object CalCVGameTask extends App {
  private val logger = LoggerFactory.getLogger(CalCVGameTask.getClass)


  logger.info("Init OK")

  CVQuery.tlp.query
    .list.asScala.to(LazyList)
    .foreach(cv => {

      val f = Future {
        GameQuery.simpleTlp.query
          .where(
            Filters.and(
              Filters.eq("gamechar.truecv", cv.name) //,
            )
          )
          .list.asScala.to(LazyList)

      }(IO_POOL)
        .map(games => {
          val start = games
            .filter(_.publishDate != null)
            .map(_.publishDate)
            .minOption

          val end = games
            .filter(_.publishDate != null)
            .map(_.publishDate)
            .maxOption

          val count = games.size

          (start, end, count)

        })(CPU_POOL)

      f.foreach { case (start: Option[LocalDate], end: Option[LocalDate], count: Int) =>
        CVDB.updateStatistics(cv, start.orNull, end.orNull, count)
      }(IO_POOL)

      Await.result(f, 10.minutes)


    })
}