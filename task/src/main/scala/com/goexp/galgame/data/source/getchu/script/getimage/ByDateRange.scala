package com.goexp.galgame.data.source.getchu.script.getimage

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.mongodb.client.model.Filters._
import com.typesafe.scalalogging.Logger

import java.time.LocalDate

object ByDateRange {
  private val logger = Logger(ByDateRange.getClass)

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      println("<start> <end>")
      return
    }
    Network.initProxy()

    val start = LocalDate.parse(args(0))
    val end = LocalDate.parse(args(1))

    logger.info(s"${start}--${end}")


    //    val start = LocalDate.of(2018, 1, 1)
    //    val end = start.plusYears(1)

    val games = GameFullQuery()
      .where(and(
        gte("publishDate", DateUtil.toDate(s"${start} 00:00:00")),
        lte("publishDate", DateUtil.toDate(s"${end} 23:59:59"))
      ))
      .scalaList().to(LazyList)


    BatchImageDownloader.download(games)
  }

}
