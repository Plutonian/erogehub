package com.goexp.galgame.data.task.local.cal

import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.task.ansyn.Pool._
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object CalBrandGameTask {
  private val logger = LoggerFactory.getLogger(CalBrandGameTask.getClass)

  def main(args: Array[String]): Unit = {

    logger.info("Init OK")

    val brands = BrandQuery.tlp.query
      .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
      .scalaList.to(LazyList)

    val futures = brands
      .map(b => {

        Future {
          GameQuery.simpleTlp.query
            .where(Filters.eq("brandId", b.id))
            .scalaList.to(LazyList)

        }(IO_POOL)
          .map { games =>
            val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
            val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

            val count = games.size

            logger.trace(s"$start, $end, $count")

            (start, end, count)

          }(CPU_POOL)
          .map { case (start, end, count) =>
            logger.trace(s"$start, $end, $count")
            BrandDB.updateStatistics(b, start.orNull, end.orNull, count)
          }(IO_POOL)

      })

    Await.result(Future.sequence(futures), Duration.Inf)
  }
}