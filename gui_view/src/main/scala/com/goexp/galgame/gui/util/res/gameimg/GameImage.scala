package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object GameImage {
  def apply(game: Game): GameImage = new GameImage(game)
}

class GameImage(private[this] val game: Game) {

  def tiny(): Image =
    Util.getImage(game, (s"${game.id}/game_t", game.smallImg))

  def small(): Image = {
    val url = GetchuGame.SmallImg(game.id)
    Util.getImage(game, (s"${game.id}/game_s", url))
  }

  def large(): Image = {
    val url = GetchuGame.LargeImg(game.id)
    Util.getImage(game, (s"${game.id}/game_l", url))
  }

  def preloadLarge(): Unit = {
    val url = GetchuGame.LargeImg(game.id)
    Util.preLoadRemoteImage((s"${game.id}/game_l", url))
  }
}