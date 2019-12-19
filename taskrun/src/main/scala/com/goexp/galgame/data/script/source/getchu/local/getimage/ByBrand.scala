package com.goexp.galgame.data.script.source.getchu.local.getimage

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameQuery}
import com.goexp.galgame.data.source.getchu.task.Util
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.{Filters, Sorts}
import com.typesafe.scalalogging.Logger

object ByBrand {
  private val logger = Logger(ByDateRange.getClass)

  def main(args: Array[String]): Unit = {

    Network.initProxy()


    val types = List(
      BrandState.LIKE,
      BrandState.CHECKING,
      BrandState.HOPE,
      BrandState.MARK,
      BrandState.NORMAL,
      BrandState.UNCHECKED
    )

    types.foreach {
      t =>
        logger.info(s"Loading... brand type:${t}")

        val brandList = BrandQuery.tlp
          .where(Filters.eq("type", t.value))
          .sort(Sorts.descending("type"))
          .scalaList()

        logger.info(s"${brandList.size} brands load OK")

        val games = brandList.to(LazyList)
          .flatMap { b =>
            logger.trace(s"Loading...game from brand:${b}")

            val glist = GameQuery.fullTlp
              .where(and(
                Filters.eq("brandId", b.id),
                Filters.ne("state", GameState.BLOCK.value),
                Filters.ne("state", GameState.SAME.value)
              ))
              .scalaList()

            logger.trace(s"${glist.size} games load OK")
            glist
          }


        Util.downloadImage(games)
    }


  }

}
