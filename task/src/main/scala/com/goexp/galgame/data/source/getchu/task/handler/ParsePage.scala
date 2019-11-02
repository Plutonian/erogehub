package com.goexp.galgame.data.source.getchu.task.handler

import java.util.Objects

import com.goexp.galgame.data.source.getchu.parser.game.{DetailPageParser, ListPageParser}
import com.goexp.piplline.handler.DefaultHandler
import com.typesafe.scalalogging.Logger

/**
  * Parse String => Game
  */
class ParsePage extends DefaultHandler {
  final private val logger = Logger(classOf[ParsePage])

  override def processEntity: PartialFunction[Any, Unit] = {

    // parse Game detail page
    case (gameId: Int, html: String) =>
      Objects.requireNonNull(html)

      val parser = new DetailPageParser
      val game = parser.parse(gameId, html)

      sendTo(classOf[Game2DB], game)

    // parse game list
    case (html: String, "ListPageParser") =>
      Objects.requireNonNull(html)

      val list = new ListPageParser().parse(html)

      logger.info(s"${list.size}")

      list.foreach { game =>
        sendTo(classOf[PreProcessGame], game)
      }

  }
}