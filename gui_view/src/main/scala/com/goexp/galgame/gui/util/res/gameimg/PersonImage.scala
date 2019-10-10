package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.gui.model.Game


class PersonImage(private[this] val game: Game) {

  def small(index: Int, src: String) = {
    val url = GetchuGameRemote.getUrlFromSrc(src)
    val local = GetchuGameLocal.gameChar(game.id, index)
    GameImages.get(game)(local, url)
  }
}
