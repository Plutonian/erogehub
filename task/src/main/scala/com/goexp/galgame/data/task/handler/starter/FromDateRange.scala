package com.goexp.galgame.data.task.handler.starter

import java.time.LocalDate
import java.util.concurrent.TimeUnit

import com.goexp.galgame.data.piplline.core.{Message, Starter}
import com.goexp.galgame.data.task.client.GetChu.GameRemote
import com.goexp.galgame.data.task.handler.PreProcessGame
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process() = {
    logger.info(s"Start:$start,End:$end")

    val list = GameRemote.from(start, end)

    logger.info(s"${list.size}")


    Range.inclusive(1, list.size)
      .foreach { num =>
        if (num % 10 == 0)
          TimeUnit.SECONDS.sleep(5L)

        val game = list(num - 1)
        send(Message(classOf[PreProcessGame].hashCode(), game))

      }


    //    list.foreach { game =>
    //      send(Message(classOf[PreProcessGame].hashCode(), game))
    //    }
  }
}