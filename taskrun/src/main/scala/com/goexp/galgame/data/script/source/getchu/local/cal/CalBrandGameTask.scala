package com.goexp.galgame.data.script.source.getchu.local.cal

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.source.getchu.importor.BrandDB
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameQuery}
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.typesafe.scalalogging.Logger


object CalBrandGameTask {
  private val logger = Logger(CalBrandGameTask.getClass)

  def main(args: Array[String]): Unit = {

    logger.info("Init OK")

    val brands = BrandQuery.tlp
      //      .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
      .scalaList()

    brands
      .foreach(brand => {

        val games = GameQuery.simpleTlp
          .where(and(
            Filters.eq("brandId", brand.id),
            Filters.ne("state", GameState.SAME.value)
          ))
          .scalaList().to(LazyList)

        logger.info(s"${brand.id} ${brand.name} ${games.size}")

        val count = games.size
        val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
        val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

        logger.trace(s"$start, $end, $count")

        BrandDB.updateStatistics(brand, start.orNull, end.orNull, count)

      })

  }
}