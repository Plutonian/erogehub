package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image


class PersonImage(private[this] val game: Game) {

  def small(index: Int): Image = {
    val key = GetchuGameLocal.gameChar(game.id, index)
    GameImages.get(game)(key, key)
  }
}
