package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object PersonImage {
  def small(game: Game, index: Int, src: String): Image = {
    val url = GetchuGame.getUrlFromSrc(src)
    GameImages.get(game)(s"${game.id}/char_s_$index", url)
  }
}