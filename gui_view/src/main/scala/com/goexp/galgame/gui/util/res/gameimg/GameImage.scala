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

  def tiny200() = {
    val local = GetchuGameLocal.tiny200Img(game.id)
    val remote = GetchuGameRemote.tiny200Img(game.id)
    GameImages.get(game)(local, remote)
  }

  def normal() = {
    val remote = GetchuGameRemote.normalImg(game.id)
    val local = GetchuGameLocal.normalImg(game.id)
    GameImages.get(game)(local, remote)
  }

  def large() = {
    val remote = GetchuGameRemote.largeImg(game.id)
    val local = GetchuGameLocal.largeImg(game.id)
    GameImages.get(game)(local, remote)
  }

}