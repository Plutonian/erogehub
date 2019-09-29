package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

class SimpleImage(private[this] val game: Game) {

  protected var onOKEvent: Image => Unit = _

  def onOK(f: Image => Unit) = {
    this.onOKEvent = f
    this
  }

  def small(index: Int, src: String) = {
    val url = GetchuGame.smallSimpleImg(src)
    GameImages.get(game)(s"${game.id}/simple_s_$index", url)(onOKEvent)
  }

  def large(index: Int, src: String) = {
    val url = GetchuGame.largeSimpleImg(src)
    GameImages.get(game)(s"${game.id}/simple_l_$index", url)(onOKEvent)
  }
}