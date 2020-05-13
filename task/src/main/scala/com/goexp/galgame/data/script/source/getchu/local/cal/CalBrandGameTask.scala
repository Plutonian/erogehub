package com.goexp.galgame.data.script.source.getchu.local.cal

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.common.model.{GameStatistics, LocationStatistics, StarStatistics, StateStatistics}
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
            Filters.eq("brandId", brand.id),
            Filters.ne("state", GameState.SAME.value)
          ))
          .scalaList().to(LazyList)

        logger.trace(s"${brand.id} ${brand.name} ${games.size}")

        val count = games.size
        val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
        val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

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


        val filterdList = games.filter { g => (g.state ne GameState.SAME) && (g.state ne GameState.BLOCK) }

        val realCount = filterdList.size


        val statMap = filterdList.groupBy { g => g.state }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap
        val starMap = filterdList.groupBy { g => g.star }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap
        val locationMap = filterdList.groupBy { g => g.location }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap

        //        val played = statMap.getOrElse(GameState.PLAYED, 0)
        //        val playing = statMap.getOrElse(GameState.PLAYING, 0)
        //        val hope = statMap.getOrElse(GameState.HOPE, 0)
        //        val viewLater = statMap.getOrElse(GameState.READYTOVIEW, 0)
        //        val uncheck = statMap.getOrElse(GameState.UNCHECKED, 0)

        val statistics =
          GameStatistics(
            start.orNull,
            end.orNull,
            count,
            realCount,
            StateStatistics(
              statMap.getOrElse(GameState.PLAYED, 0),
              statMap.getOrElse(GameState.PLAYING, 0),
              statMap.getOrElse(GameState.HOPE, 0),
              statMap.getOrElse(GameState.READYTOVIEW, 0),
              statMap.getOrElse(GameState.UNCHECKED, 0)
            ), StarStatistics(
              starMap.getOrElse(0, 0),
              starMap.getOrElse(1, 0),
              starMap.getOrElse(2, 0),
              starMap.getOrElse(3, 0),
              starMap.getOrElse(4, 0),
              starMap.getOrElse(5, 0)
            ), LocationStatistics(
              locationMap.getOrElse(GameLocation.LOCAL, 0),
              locationMap.getOrElse(GameLocation.NETDISK, 0),
              locationMap.getOrElse(GameLocation.REMOTE, 0)
            )
          )


        if (tags.nonEmpty)
          logger.trace(s"Brand:${brand.name} \tTags :${tags.mkString(",")}\n")

        logger.trace(s"$start, $end, $count")

        BrandDB.updateStatistics(brand, tags.asJava, statistics)

      })

  }
}