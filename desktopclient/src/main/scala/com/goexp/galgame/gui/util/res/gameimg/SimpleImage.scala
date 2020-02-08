package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

class SimpleImage(private val game: Game) {

  def small(index: Int): Image = {
    val key = GetchuGameLocal.smallSimpleImg(game, index)
    GameImages.get(game)(key, key)
  }

  def large(index: Int): Image = {
    val key = GetchuGameLocal.largeSimpleImg(game, index)
    GameImages.get(game)(key, key)
  }
}