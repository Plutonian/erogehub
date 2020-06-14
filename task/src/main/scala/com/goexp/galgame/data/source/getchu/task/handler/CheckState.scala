package com.goexp.galgame.data.source.getchu.task.handler

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.task.handler.DownloadImage.ImageParam
import com.goexp.piplline.handler.DefaultActor


object CheckState {
  private val SkipState = Set(GameState.SAME, GameState.BLOCK)
}

class CheckState extends DefaultActor {
  override def receive: Rec = {
    case game: Game =>
      // check game state

      import CheckState.SkipState


      //State not skip
      if (!SkipState.contains(game.state)) {

        val imgs = game.allImgs

        if (imgs.nonEmpty) {
          logger.info(s"DownloadImage for Game[${game.id}] ${game.name} [${game.publishDate}] ${game.state}")
          imgs.foreach { case (path, local) =>
            sendTo[DownloadImage](ImageParam(path, local))
          }
        }
      }

  }
}
