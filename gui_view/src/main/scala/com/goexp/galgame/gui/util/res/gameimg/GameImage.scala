package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object GameImage {
  def apply(game: Game): GameImage = new GameImage(game)
}

class GameImage(private[this] val game: Game) {
  private val images = GameImages(game)

  def onOK(f: (Image) => Unit) = {
    images.onOK = f
    this
  }

  def tiny() = {
    images.get(s"${game.id}/game_t", game.smallImg)
  }

  def small() = {
    val url = GetchuGame.SmallImg(game.id)
    images.get(s"${game.id}/game_s", url)
  }

  def large() = {
    val url = GetchuGame.LargeImg(game.id)
    images.get(s"${game.id}/game_l", url)
  }

}