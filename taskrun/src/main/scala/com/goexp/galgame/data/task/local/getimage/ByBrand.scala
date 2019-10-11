package com.goexp.galgame.data.task.local.getimage

import com.goexp.galgame.common.model.{BrandType, GameState}
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.{and, not}
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object ByBrand {
  private val logger = LoggerFactory.getLogger(ByDateRange.getClass)

  def main(args: Array[String]): Unit = {

    Network.initProxy()


    val brandList = BrandQuery.tlp.query
      .where(Filters.eq("type", BrandType.HOPE.value))
      .list

    logger.info("Brands:{}", brandList.size())

    val games = brandList.asScala.to(LazyList)
      .flatMap { b =>
        GameQuery.fullTlp.query
          .where(and(
            Filters.eq("brandId", b.id),
            not(Filters.eq("state", GameState.BLOCK.value)),
            not(Filters.eq("state", GameState.SAME.value))
          ))
          .list.asScala
      }


    Util.downloadImage(games)
  }

}
