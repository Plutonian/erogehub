package com.goexp.galgame.data.source.erogamescape.task

import java.io.IOException
import java.util.concurrent.CompletionException

import com.goexp.galgame.data.source.erogamescape.GameRemote
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory


/**
  * Check game is new or already has
  */
class GetGameList extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[GetGameList])

  override def process(message: Message) = {

    message.entity match {
      case year: Int =>

        logger.info(s"Get $year: ")
        try {
          val list = GameRemote.from(year)

          logger.info(s"Get $year: (${list.size})")

          list
            .foreach {
              g =>
                send(classOf[PreProcessGame].hashCode(), g)
            }
        }
        catch {
          case e: CompletionException if (e.getCause.isInstanceOf[IOException]) =>
            logger.error(e.getCause.getMessage)
            send(classOf[GetGameList].hashCode(), year)
        }
    }
  }


}

