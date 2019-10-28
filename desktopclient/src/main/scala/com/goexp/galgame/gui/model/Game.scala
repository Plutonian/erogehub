package com.goexp.galgame.gui.model

import java.util.StringJoiner

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.{CommonGame, GameState}
import javafx.beans.property.SimpleObjectProperty

import scala.beans.BeanProperty

class Game extends CommonGame {
  var state = new SimpleObjectProperty[GameState]
  var brand: Brand = null

  @BeanProperty
  var star = 0

  def getWriter: String = String.join(",", writer)

  def getPainter: String = String.join(",", painter)

  def getType: String = String.join(",", `type`)

  def getState = state.get

  def setState(isLike: GameState) = this.state.set(isLike)

  def stateProperty = state


  override def toString = infoView

  def infoView: String = {
    new StringJoiner(", ", classOf[Game].getSimpleName + "[", "]")
      .add("id=" + RED.s(String.valueOf(id)))
      .add("name='" + RED.s(name) + "'")
      .add("publishDate=" + publishDate)
      .add("smallImg='" + smallImg + "'")
      .add("writer=" + writer)
      .add("painter=" + painter)
      .add("type=" + `type`)
      .add("tag=" + tag)
      .add("state=" + state.get)
      .add("star=" + star)
      .add("\nbrand=" + brand)
      .add("\ngameImgs=" + Option(gameImgs).map(_.size).getOrElse(0))
      .add("\ngameCharacters=" + gameCharacters)
      .toString
  }

  def isOkState = Option(this.state).map(_.get).exists(gs => gs.value >= GameState.UNCHECKED.value || (gs eq GameState.PACKAGE))

  def isOkImg = Strings.isNotEmpty(smallImg) && smallImg.startsWith("http")
}