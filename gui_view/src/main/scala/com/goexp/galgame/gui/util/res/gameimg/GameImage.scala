package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.common.website.getchu.GetchuGame
import com.goexp.galgame.gui.model.Game
import javafx.scene.image.Image

object GameImage {
  def apply(game: Game): GameImage = new GameImage(game)
}

class GameImage(private[this] val game: Game)  {

  protected var onOKEvent: Image => Unit = _

  def onOK(f: Image => Unit) = {
    this.onOKEvent = f
    this
  }

  def tiny() = {
    GameImages.get(game)(s"${game.id}/game_t", game.smallImg)(onOKEvent)
  }

  def small() = {
    val url = GetchuGame.SmallImg(game.id)
    GameImages.get(game)(s"${game.id}/game_s", url)(onOKEvent)
  }

  def large() = {
    val url = GetchuGame.LargeImg(game.id)
    GameImages.get(game)(s"${game.id}/game_l", url)(onOKEvent)
  }

}