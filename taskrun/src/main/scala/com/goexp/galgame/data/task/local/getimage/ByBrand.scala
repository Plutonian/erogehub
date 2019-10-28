package com.goexp.galgame.data.task.local.getimage

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.model.game.brand.BrandType
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.task.handler.Util
import com.mongodb.client.model.Filters.{and, not}
import com.mongodb.client.model.{Filters, Sorts}
import org.slf4j.LoggerFactory

object ByBrand {
  private val logger = LoggerFactory.getLogger(ByDateRange.getClass)

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
                not(Filters.eq("state", GameState.BLOCK.value)),
                not(Filters.eq("state", GameState.SAME.value))
              ))
              .scalaList()

            logger.trace("{} games load OK", glist.size)
            glist
          }


        Util.downloadImage(games)
    }


  }

}
