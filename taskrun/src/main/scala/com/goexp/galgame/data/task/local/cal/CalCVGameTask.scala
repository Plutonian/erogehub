package com.goexp.galgame.data.task.local.cal

import com.goexp.galgame.data.db.importor.mongdb.CVDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.goexp.galgame.data.task.ansyn.Pool._
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters._

object CalCVGameTask {
  private val logger = LoggerFactory.getLogger(CalCVGameTask.getClass)

  def main(args: Array[String]): Unit = {

    val cvList = CVQuery.tlp.query.list.asScala.to(LazyList)
    logger.info("Init OK")

    val futures = cvList.map(cv => {

      Future {
        GameQuery.simpleTlp.query
          .where(Filters.eq("gamechar.truecv", cv.name))
          .list.asScala.to(LazyList)
      }(IO_POOL)
        .map { games =>
          val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
          val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

          val count = games.size

          (start, end, count)

        }(CPU_POOL)
        .map {
          case (start, end, count) =>
            logger.trace(s"$start, $end, $count")
            CVDB.updateStatistics(cv, start.orNull, end.orNull, count)
        }(IO_POOL)
    })

    Await.result(Future.sequence(futures), Duration.Inf)
  }
}