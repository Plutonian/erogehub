package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.gui.model.Game

object GameImage {
  def apply(game: Game): GameImage = new GameImage(game)
}

class GameImage(private[this] val game: Game) {

  def tiny() = {
    val local = GetchuGameLocal.tiny120Img(game.id)
    val remote = game.smallImg
    GameImages.get(game)(local, remote)
  }

  def small() = {
    val remote = GetchuGameRemote.smallImg(game.id)
    val local = GetchuGameLocal.smallImg(game.id)
    GameImages.get(game)(local, remote)
  }

  def large() = {
    val remote = GetchuGameRemote.largeImg(game.id)
    val local = GetchuGameLocal.largeImg(game.id)
    GameImages.get(game)(local, remote)
  }

}