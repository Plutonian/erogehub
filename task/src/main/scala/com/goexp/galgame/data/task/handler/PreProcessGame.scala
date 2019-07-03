package com.goexp.galgame.data.task.handler

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import org.slf4j.LoggerFactory

class PreProcessGame extends DefaultMessageHandler[Game] {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])

  override def process(message: Message[Game]) = {
    val game = message.entity
    logger.debug("<Game> {}", game)
    if (GameDB.exist(game.id)) {
      logger.debug("<Update> {}", game.simpleView)
      GameDB.update(game)
    }
    else {
      game.state = GameState.UNCHECKED
      game.isNew = true
      logger.info("<Insert> {}", game.simpleView)
      GameDB.insert(game)
    }
    send(new Message[Int](MesType.NEED_DOWN_GAME, game.id))
  }
}