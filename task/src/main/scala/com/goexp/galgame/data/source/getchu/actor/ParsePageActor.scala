package com.goexp.galgame.data.source.getchu.actor

import java.util.Objects

import com.goexp.galgame.data.source.getchu.parser.game.{DetailPageParser, ListPageParser}
import com.goexp.piplline.handler.DefaultActor

/**
 * Parse String => Game
 */
class ParsePageActor extends DefaultActor {

  override def receive = {

    // parse Game detail page
    case (gameId: Int, html: String) =>
      Objects.requireNonNull(html)

      val parser = new DetailPageParser
      val game = parser.parse(gameId, html)

      if (game.brandId == 0) {
        logger.error(s"Get brandid error Game[${game.id}] ${game.name}")
      } else {

        sendTo[SaveGameActor](game)
      }


    // parse game list
    case (html: String, "ListPageParser") =>
      Objects.requireNonNull(html)

      val list = new ListPageParser().parse(html)

      logger.info(s"${list.size}")

      list.foreach { game =>
        sendTo[InsertOrUpdateGameActor](game)
      }

  }
}