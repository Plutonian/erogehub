package com.goexp.galgame.data.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultStarter
import com.goexp.galgame.data.task.client.GetChu
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends DefaultStarter[Int] {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process(msgQueue: MessageQueueProxy[Message[_]]) = {
    logger.info(s"Start:$start,End:$end")

    val list = GetChu.BrandService.gamesFrom(start, end)

    logger.info(s"${list.size}")
    list.foreach(game => {
      msgQueue.offer(new Message[Game](MesType.PRE_GAME, game))
    })
  }
}