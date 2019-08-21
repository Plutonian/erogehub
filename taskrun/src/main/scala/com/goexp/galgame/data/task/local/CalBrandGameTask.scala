package com.goexp.galgame.data.task.local

import java.time.LocalDate

import com.goexp.galgame.common.model.{BrandType, GameState}
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object CalBrandGameTask {
  private val logger = LoggerFactory.getLogger(CalBrandGameTask.getClass)


  def main(args: Array[String]) = {

    logger.info("Init OK")

    BrandQuery.tlp.query
      .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
      .list.asScala.to(LazyList)
      .foreach(b => {
        val games = GameQuery.simpleTlp.query
          .where(Filters.and(Filters.eq("brandId", b.id), Filters.ne("state", GameState.SAME.value)))
          .list

        val start = games.asScala.to(LazyList)
          .filter(_.publishDate != null)
          .map(_.publishDate)
          .minOption.orNull

        val end = games.asScala.to(LazyList)
          .filter(_.publishDate != null)
          .map(_.publishDate)
          .maxOption.orNull

        val count = games.asScala.to(LazyList)
          .filter(_.publishDate != null)
          .map(_.publishDate)
          .size

        BrandDB.updateStatistics(b, start, end, count)


      })
  }
}