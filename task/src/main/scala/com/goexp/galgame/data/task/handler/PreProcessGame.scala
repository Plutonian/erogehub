package com.goexp.galgame.data.task.handler

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.model.Game
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

/**
  * Check game is new or already has
  */
class PreProcessGame extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])

  override def process(message: Message) = {

    message.entity match {
      case game: Game =>

        //already has
        if (GameDB.exist(game.id)) {
          logger.debug("<Update> {}", game.simpleView)
          GameDB.update(game)
        }
        else {
          //new game

          game.state = GameState.UNCHECKED
          game.isNew = true
          logger.info("<Insert> {}", game.simpleView)
          GameDB.insert(game)
        }
        send(Message(classOf[DownloadGameHandler].hashCode(), game.id))
    }
  }
}