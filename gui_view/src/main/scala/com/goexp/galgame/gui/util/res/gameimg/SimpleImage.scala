package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object SimpleImage {
  def small(game: Game, index: Int, src: String): Image = {
    val url = GetchuGame.smallSimpleImg(src)
    Util.getImage(game)(s"${game.id}/simple_s_$index", url)
  }

  def large(game: Game, index: Int, src: String): Image = {
    val url = GetchuGame.largeSimpleImg(src)
    Util.getImage(game)(s"${game.id}/simple_l_$index", url)
  }
}