package com.goexp.galgame.data.task.handler.game

import java.util.Objects

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.ParseException
import com.goexp.galgame.data.parser.game.DetailPageParser
import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

/**
  *Parse String => Game
  */
class Html2GameOK extends DefaultMessageHandler[(Int, String)] {
  final private val logger = LoggerFactory.getLogger(classOf[Html2GameOK])

  override def process(message: Message[(Int, String)]) = {
    val (gameId, html) = message.entity

    logger.debug("<Html2GameOK> {}", gameId)

    Objects.requireNonNull(html)

    try {

      val parser = new DetailPageParser
      val game = parser.parse(gameId, html)
      send(Message[Game](MesType.GAME_OK, game))
    } catch {
      case e: ParseException =>
        e.printStackTrace()
    }
  }
}