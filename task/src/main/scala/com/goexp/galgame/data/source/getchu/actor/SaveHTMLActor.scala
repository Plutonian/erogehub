package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.Config
import com.goexp.galgame.data.model.Game
import com.goexp.pipeline.handler.DefaultActor

import java.nio.file.Files

class SaveHTMLActor extends DefaultActor {
  override def receive = {
    // parse Game detail page
    case (game: Game, html: String) => {

      val (year, month) = Option(game.publishDate).map { d => (d.getYear, d.getMonthValue) }.getOrElse((0, 0))


      val local = Config.IMG_LOCAL_ROOT.resolve(s"$year/$month/${game.id}/game.html")

      Files.createDirectories(local.getParent)
      Files.write(local, html.getBytes)
    }
  }

}
