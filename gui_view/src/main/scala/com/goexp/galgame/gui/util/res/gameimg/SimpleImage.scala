package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.gui.model.Game

class SimpleImage(private[this] val game: Game) {

  def small(index: Int, src: String) = {
    val remote = GetchuGameRemote.smallSimpleImg(src)
    val local = GetchuGameLocal.smallSimpleImg(game.id, index)
    GameImages.get(game)(local, remote)
  }

  def large(index: Int, src: String) = {
    val remote = GetchuGameRemote.largeSimpleImg(src)
    val local = GetchuGameLocal.largeSimpleImg(game.id, index)
    GameImages.get(game)(local, remote)
  }
}