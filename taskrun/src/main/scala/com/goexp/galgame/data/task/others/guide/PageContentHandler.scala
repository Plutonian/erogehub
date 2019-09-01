package com.goexp.galgame.data.task.others.guide

import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.data.db.importor.mongdb.GuideDB
import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import org.slf4j.LoggerFactory

class PageContentHandler extends DefaultMessageHandler {
  private val logger = LoggerFactory.getLogger(classOf[PageContentHandler])

  override def process(message: Message) = {
    message.entity match {
      case guide: CommonGame.Guide =>
        logger.info(s"insert:$guide")
        GuideDB.insert(guide)
    }

  }
}
