package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import DownloadImageActor.ImageParam
import com.goexp.piplline.handler.DefaultActor


object CheckStateActor {
  private val SkipState = Set(GameState.SAME, GameState.BLOCK)
}

class CheckStateActor extends DefaultActor {
  override def receive: Rec = {
    case game: Game =>
      // check game state

      import CheckStateActor.SkipState


      //State not skip
      if (!SkipState.contains(game.state)) {

        val imgs = game.allImgs

        if (imgs.nonEmpty) {
          logger.info(s"DownloadImage [${game.id}] ${game.name} [${game.publishDate}] ${game.state}")
          imgs.foreach { case (path, local) =>
            sendTo[DownloadImageActor](ImageParam(path, local))
          }
        } else {
          logger.info(s"Image already in local [${game.id}] ${game.name} [${game.publishDate}] ${game.state}")
        }
      } else {
        logger.info(s"Skip download image [${game.id}] ${game.name} [${game.publishDate}] ${game.state}")
      }
  }
}
