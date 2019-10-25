package com.goexp.galgame.data.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.task.client.GetChu.GameRemote
import com.goexp.galgame.data.task.handler.PreProcessGame
import com.goexp.piplline.core.{Message, Starter}
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process() = {
    logger.info(s"Start:$start,End:$end")

    val list = GameRemote.from(start, end)

    logger.info(s"${list.size}")


    Range.inclusive(1, list.size).foreach { num =>

        val game = list(num - 1)
        send(Message(classOf[PreProcessGame].hashCode(), game))

    }


  }
}