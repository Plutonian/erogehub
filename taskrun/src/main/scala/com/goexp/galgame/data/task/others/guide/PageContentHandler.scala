package com.goexp.galgame.data.task.others.guide

import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.data.db.importor.mongdb.GuideDB
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import org.slf4j.LoggerFactory

class PageContentHandler extends DefaultMessageHandler[CommonGame.Guide] {
  private val logger = LoggerFactory.getLogger(classOf[PageContentHandler])

  override def process(message: Message[CommonGame.Guide]) = {
    val guide = message.entity
    logger.info(s"insert:$guide")
    GuideDB.insert(guide)
  }
}
