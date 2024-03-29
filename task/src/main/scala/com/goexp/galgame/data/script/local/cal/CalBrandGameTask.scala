package com.goexp.galgame.data.script.local.cal

import com.goexp.common.util.string.Strings
import com.goexp.galgame.data.script.local.cal.GameStat.calStat
import com.goexp.galgame.data.source.getchu.importor.BrandDB
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameSimpleQuery}
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object CalBrandGameTask {
  private val logger = Logger(CalBrandGameTask.getClass)

  def main(args: Array[String]): Unit = {

    logger.info("Init OK")

    val brands = BrandQuery()
      //      .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
      .scalaList()

    brands
      .foreach(brand => {

        val games = GameSimpleQuery()
          .where(and(
            Filters.eq("brandId", brand.id)
          ))
          .scalaList().to(LazyList)

        logger.trace(s"${brand.id} ${brand.name} ${games.size}")

        val tags = games
          .filter {
            _.tag != null
          }
          .flatMap {
            _.tag.asScala
          }

          .filter { s => Strings.isNotEmpty(s) }
          .filter { s => !Set("ファンディスク", "アニメーション", "学園", "女子学生").contains(s) }
          .groupBy { s => s }.to(LazyList)

          .map { case (k, v) => (k, v.size) }
          .sortBy { case (_, size) => size }.reverse
          .take(5) //top 5
          .map { case (k, _) => k }

        val statistics = calStat(games)


        if (tags.nonEmpty)
          logger.trace(s"Brand:${brand.name} \tTags :${tags.mkString(",")}\n")

        logger.trace(s"$statistics")

        BrandDB.updateStatistics(brand, tags.asJava, statistics)

      })

  }

}