package com.goexp.galgame.data.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.piplline.core.{Message, Starter}
import com.goexp.galgame.data.task.client.GetChu.GameService
import com.goexp.galgame.data.task.handler.PreProcessGame
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process() = {
    logger.info(s"Start:$start,End:$end")

    val list = GameService.from(start, end)

    logger.info(s"${list.size}")
    list.foreach(game => {
      send(Message(classOf[PreProcessGame].hashCode(), game))
    })
  }
}