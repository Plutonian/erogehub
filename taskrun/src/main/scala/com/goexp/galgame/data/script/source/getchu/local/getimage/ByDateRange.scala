package com.goexp.galgame.data.script.source.getchu.local.getimage

import java.time.LocalDate

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.query.GameQuery
import com.goexp.galgame.data.source.getchu.task.Util
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import org.slf4j.LoggerFactory

object ByDateRange {
  private val logger = LoggerFactory.getLogger(ByDateRange.getClass)

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      println("<start> <end>")
      return
    }
    Network.initProxy()

    val start = LocalDate.parse(args(0))
    val end = LocalDate.parse(args(1))

    logger.info("{}--{}", start, end)


    //    val start = LocalDate.of(2018, 1, 1)
    //    val end = start.plusYears(1)

    val games = GameQuery.fullTlp
      .where(and(
        gte("publishDate", DateUtil.toDate(s"${start} 00:00:00")),
        lte("publishDate", DateUtil.toDate(s"${end} 23:59:59")),
        Filters.ne("state", GameState.BLOCK.value),
        Filters.ne("state", GameState.SAME.value)
      ))
      .scalaList().to(LazyList)


    Util.downloadImage(games)
  }

}
