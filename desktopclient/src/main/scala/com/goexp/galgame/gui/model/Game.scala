package com.goexp.galgame.gui.model

import java.util.StringJoiner

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.{StringOption, Strings}
import com.goexp.galgame.common.model.game.{CommonGame, GameLocation, GameState}
import javafx.beans.property.SimpleObjectProperty

import scala.beans.BeanProperty

class Game extends CommonGame {
  lazy val state = new SimpleObjectProperty[GameState]
  lazy val location = new SimpleObjectProperty[GameLocation]
  var brand: Brand = _

  @BeanProperty
  var star = 0

  def getWriter: String = String.join(",", writer)

  def getPainter: String = String.join(",", painter)

  def getType: String = String.join(",", `type`)

  def getState = state.get

  def setState(state: GameState) = this.state.set(state)

  def stateProperty = state

  def getLocation = location.get

  def setLocation(location: GameLocation) = this.location.set(location)

  def locationProperty = location


  override def toString = s"Game[${RED.s(id.toString)}] ${RED.s(name)} Date:${publishDate} img:${smallImg}  state:<${
    Option(state).map {
      _.get
    }.getOrElse("--")
  }>"

  def infoView: String = {
    new StringJoiner(", ", classOf[Game].getSimpleName + "[", "]")
      .add("id=" + RED.s(String.valueOf(id)))
      .add("name='" + RED.s(name) + "'")
      .add("publishDate=" + publishDate)
      //      .add("writer=" + writer)
      //      .add("painter=" + painter)
      //      .add("type=" + `type`)
      //      .add("tag=" + tag)
      .add("state=" + state.get)
      .add("smallImg='" + smallImg + "'")
      //      .add("star=" + star)
      .add("\nbrand=" + brand)
      .add("\ngameImgs=" + Option(gameImgs).map(_.size).getOrElse(0))
      .add("\ngameCharacters=" + gameCharacters)
      .toString
  }

  def isOkImg = StringOption(smallImg).exists(_.startsWith("http"))
}