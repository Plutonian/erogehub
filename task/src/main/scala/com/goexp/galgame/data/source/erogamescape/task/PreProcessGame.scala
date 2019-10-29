package com.goexp.galgame.data.source.erogamescape.task

import com.goexp.galgame.data.source.erogamescape.importor.GameDB
import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser.PageItem
import com.goexp.galgame.data.source.erogamescape.task.game.GetGameBasic
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory


/**
  * Check game is new or already has
  */
class PreProcessGame extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])

  override def process(message: Message) = {

    message.entity match {
      case pageItem: PageItem =>

        //already has
        //        if (GameDB.exist(pageItem.id)) {
        //          logger.debug("<Update> {}", pageItem.simpleView)
        //          GameDB.update(pageItem)
        //        }
        //        else {
        //new pageItem
        logger.info("<Insert> {} {}", pageItem)
        GameDB.insert(pageItem)

        send(classOf[GetGameBasic].hashCode(), pageItem.id)
    }
  }
}
