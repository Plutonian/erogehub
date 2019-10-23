package com.goexp.galgame.data.task.handler.game

import java.util.Objects

import com.goexp.galgame.data.parser.ParseException
import com.goexp.galgame.data.parser.game.DetailPageParser
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

/**
  * Parse String => Game
  */
class Html2GameOK extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[Html2GameOK])

  override def process(message: Message) = {
    message.entity match {
      case (gameId: Int, html: String) =>

        //        logger.debug("<Html2GameOK> {}", gameId)

        Objects.requireNonNull(html)

        try {

          val parser = new DetailPageParser
          val game = parser.parse(gameId, html)
          send(Message(classOf[Game2DB].hashCode(), game))
        } catch {
          case e: ParseException =>
            e.printStackTrace()
        }
    }
  }
}