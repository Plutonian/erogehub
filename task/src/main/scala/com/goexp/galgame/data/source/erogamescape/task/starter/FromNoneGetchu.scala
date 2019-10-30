package com.goexp.galgame.data.source.erogamescape.task.starter

import com.goexp.galgame.data.source.erogamescape.query.GameQuery
import com.goexp.galgame.data.source.erogamescape.task.game.GetGameBasic
import com.goexp.piplline.core.Starter
import com.mongodb.client.model.{Filters, Sorts}
import org.slf4j.LoggerFactory

class FromNoneGetchu extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromNoneGetchu])

  override def process() = {
    val list = GameQuery.simpleTlp
      .where(
        Filters.and(
          Filters.eq("getchuId", 0),
          Filters.not(Filters.exists("publishDate")),
        ))
      .sort(Sorts.descending("_id")).scalaList()

    logger.info(s"Size: ${list.size}")

    list.foreach { g =>
      logger.info("Send {}", g.id)
      send(classOf[GetGameBasic].hashCode(), g.id)

    }

  }
}