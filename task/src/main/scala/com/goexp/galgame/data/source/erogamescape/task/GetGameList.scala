package com.goexp.galgame.data.source.erogamescape.task

import com.goexp.galgame.data.source.erogamescape.GameRemote
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory


/**
  * Check game is new or already has
  */
class GetGameList extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])

  override def process(message: Message) = {

    message.entity match {
      case year: Int =>

        logger.info(s"Get $year: ")

        GameRemote.from(year)
          .foreach {
            g =>
              send(classOf[PreProcessGame].hashCode(), g)
          }
    }
  }


}

