package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.source.getchu.parser.game.{DetailPageParser, ListPageParser}
import com.goexp.pipeline.handler.DefaultActor

import java.util.Objects

/**
 * Parse String => Game
 */
class ParsePageActor extends DefaultActor {

  override def receive = {

    // parse Game detail page
    case (gameId: Int, html: String) =>
      Objects.requireNonNull(html)

      try {
        val game = new DetailPageParser().parse(gameId, html)

        if (game.brandId == 0) {
          logger.error(s"Get brandid error Game[${game.id}] ${game.name}")
        } else {
          sendTo[SaveGameActor](game)
        }
      }
      catch {
        case _: NullPointerException =>
          logger.error(s"Get Game Null [Id]:$gameId\n HTML \n\n$html")
      }





    // parse game list
    case (html: String, "ListPageParser") =>
      Objects.requireNonNull(html)

      val list = new ListPageParser().parse(html)

      logger.info(s"${list.size}")

      list.foreach { item =>
        sendTo[InsertOrUpdateGameActor](item)
      }

  }
}