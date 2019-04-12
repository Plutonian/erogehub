package com.goexp.galgame.data.task.handler.game

import java.util

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.ParseException
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class Html2GameOK extends DefaultMessageHandler[util.Map.Entry[Integer, String]] {
  final private val logger = LoggerFactory.getLogger(classOf[Html2GameOK])

  override def process(message: Message[util.Map.Entry[Integer, String]], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
    val entry = message.entity
    val html = entry.getValue
    val gameId = entry.getKey
    logger.debug("<Html2GameOK> {}", gameId)
    try {
      val game = GetChu.GameService.getFrom(gameId, html)
      msgQueue.offer(new Message[Game](MesType.GAME_OK, game))
    } catch {
      case e: ParseException =>
        e.printStackTrace()
    }
  }
}