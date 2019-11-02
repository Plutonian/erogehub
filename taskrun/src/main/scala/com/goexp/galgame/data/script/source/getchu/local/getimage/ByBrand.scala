package com.goexp.galgame.data.script.source.getchu.local.getimage

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.model.game.brand.BrandType
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
      BrandType.LIKE,
      BrandType.CHECKING,
      BrandType.HOPE,
      BrandType.MARK,
      BrandType.NORMAL,
      BrandType.UNCHECKED
    )

    types.foreach {
      t =>
        logger.info("Loading... brand type:{}", t)

        val brandList = BrandQuery.tlp
          .where(Filters.eq("type", t.value))
          .sort(Sorts.descending("type"))
          .scalaList()

        logger.info("{} brands load OK", brandList.size)

        val games = brandList.to(LazyList)
          .flatMap { b =>
            logger.trace("Loading...game from brand:{}", b)

            val glist = GameQuery.fullTlp
              .where(and(
                Filters.eq("brandId", b.id),
                Filters.ne("state", GameState.BLOCK.value),
                Filters.ne("state", GameState.SAME.value)
              ))
              .scalaList()

            logger.trace("{} games load OK", glist.size)
            glist
          }


        Util.downloadImage(games)
    }


  }

}
