package com.goexp.galgame.data.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.handler.DefaultStarter
import com.goexp.galgame.data.task.client.GetChu.GameService
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends DefaultStarter {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process() = {
    logger.info(s"Start:$start,End:$end")

    val list = GameService.from(start, end)

    logger.info(s"${list.size}")
    list.foreach(game => {
      send(Message[Game](MesType.PRE_GAME, game))
    })
  }
}