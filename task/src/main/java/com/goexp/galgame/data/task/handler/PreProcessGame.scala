package com.goexp.galgame.data.task.handler

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import org.slf4j.LoggerFactory

class PreProcessGame extends DefaultMessageHandler[Game] {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])
  final private val importor = new GameDB

  override def process(message: Message[Game], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
    val game = message.entity
    logger.debug("<Game> {}", game)
    if (importor.exist(game.id)) {
      logger.debug("<Update> {}", game.simpleView)
      importor.update(game)
    }
    else {
      game.state = GameState.UNCHECKED
      logger.info("<Insert> {}", game.simpleView)
      importor.insert(game)
    }
    msgQueue.offer(new Message[Int](MesType.NEED_DOWN_GAME, game.id))
  }
}