package com.goexp.galgame.data.source.erogamescape.task.game

import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

/**
  * Check game is new or already has
  */
class Html2GameBasic extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[Html2GameBasic])

  override def process(message: Message) = {

    message.entity match {
      case (id: Int, html: String) =>
        send(classOf[SaveGameBasic].hashCode(), (id, DetailPageParser.parse(html)))
    }
  }
}
