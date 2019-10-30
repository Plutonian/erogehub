package com.goexp.galgame.data.source.erogamescape.task.game

import com.goexp.galgame.data.source.erogamescape.importor.GameDB
import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser.BasicItem
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

//DB
class SaveGameBasic extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[SaveGameBasic])

  override def process(message: Message) = {

    message.entity match {
      case (id: Int, BasicItem(outLink, tag, group, date)) =>

        logger.info(s"Save ${id} ${outLink}")
        GameDB.updateContent(id, outLink, tag, group, date)
    }
  }
}
