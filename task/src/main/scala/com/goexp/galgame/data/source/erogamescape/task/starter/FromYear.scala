package com.goexp.galgame.data.source.erogamescape.task.starter

import com.goexp.galgame.data.source.erogamescape.task.GetGameList
import com.goexp.piplline.core.{Message, Starter}
import org.slf4j.LoggerFactory

class FromYear(val range: Range) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromYear])

  override def process() = {
    logger.info(s"Start:${range.start},End:${range.end}")

    //    val list = GameRemote.from(start, end)

    //    logger.info(s"${list.size}")


    //    Range.inclusive(1, list.size).foreach { num =>
    //
    //      val game = list(num - 1)
    range.foreach {
      year =>
        send(Message(classOf[GetGameList].hashCode(), year))
    }

  }
}