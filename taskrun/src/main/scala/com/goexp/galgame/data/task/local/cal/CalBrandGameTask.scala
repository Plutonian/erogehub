package com.goexp.galgame.data.task.local.cal

import java.time.LocalDate

import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters._


object CalBrandGameTask extends App {
  private val logger = LoggerFactory.getLogger(CalBrandGameTask.getClass)

  logger.info("Init OK")

  BrandQuery.tlp.query
    .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
    .list.asScala.to(LazyList)
    .foreach(b => {

      val f = Future {

        GameQuery.simpleTlp.query
          .where(
            Filters.and(
              Filters.eq("brandId", b.id) //,
              //              Filters.ne("state", GameState.SAME.value),
              //              Filters.ne("state", GameState.BLOCK.value)
            )
          )
          .list.asScala.to(LazyList)

      }(ioPool)
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

        })(cpuPool)

      f.foreach {
        case (start: Option[LocalDate], end: Option[LocalDate], count: Int) =>
          BrandDB.updateStatistics(b, start.orNull, end.orNull, count)
        case _ =>
      }(ioPool)


      Await.result(f, 10.minutes)


    })
}