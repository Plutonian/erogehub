package com.goexp.galgame.data.source.getchu.task.handler.game

import java.util.Objects

import com.goexp.galgame.data.source.getchu.parser.ParseException
import com.goexp.galgame.data.source.getchu.parser.game.DetailPageParser
import com.goexp.piplline.handler.DefaultHandler
import org.slf4j.LoggerFactory

/**
  * Parse String => Game
  */
class Html2GameOK extends DefaultHandler {
  final private val logger = LoggerFactory.getLogger(classOf[Html2GameOK])

  override def processEntity: PartialFunction[Any, Unit] = {
    case (gameId: Int, html: String) =>

      Objects.requireNonNull(html)

      try {

        val parser = new DetailPageParser
        val game = parser.parse(gameId, html)
        sendTo(classOf[Game2DB], game)
      } catch {
        case e: ParseException =>
          e.printStackTrace()
      }

  }
}