package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

class SimpleImage(private[this] val game: Game) {
  private val images = GameImages(game)

  def onOK(f: (Image) => Unit) = {
    images.onOK = f
    this
  }

  def small(index: Int, src: String) = {
    val url = GetchuGame.smallSimpleImg(src)
    images.get(s"${game.id}/simple_s_$index", url)
  }

  def large(index: Int, src: String) = {
    val url = GetchuGame.largeSimpleImg(src)
    images.get(s"${game.id}/simple_l_$index", url)
  }
}