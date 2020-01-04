package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object GameImage {
  def apply(game: Game): GameImage = new GameImage(game)
}

class GameImage(private val game: Game) {

  def tiny(): Image = {
    val key = GetchuGameLocal.tiny120Img(game.id)
    GameImages.get(game)(key, key)
  }

  def tiny200(): Image = {
    val key = GetchuGameLocal.tiny200Img(game.id)
    GameImages.get(game)(key, key)
  }

  def normal(): Image = {
    val key = GetchuGameLocal.normalImg(game.id)
    GameImages.get(game)(key, key)
  }

  def large(): Image = {
    val key = GetchuGameLocal.largeImg(game.id)
    GameImages.get(game)(key, key)
  }

}