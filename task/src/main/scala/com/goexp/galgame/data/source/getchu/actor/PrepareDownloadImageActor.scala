package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.actor.DownloadImageActor.{ImageParam, allCount}
import com.goexp.piplline.handler.DefaultActor


class PrepareDownloadImageActor extends DefaultActor {
  override def receive: Rec = {
    case (game: Game) =>

      val imgs = game.allImgs

      if (imgs.nonEmpty) {
        logger.info(s"Prepare ${game.simpleView} (${imgs.size})")

        allCount.getAndAdd(imgs.size)

        imgs.foreach { case (path, local) =>
          sendTo[DownloadImageActor](ImageParam(path, local))
        }
      } else {
        logger.debug(s"Image already in local ${game.simpleView}")
      }
  }
}
