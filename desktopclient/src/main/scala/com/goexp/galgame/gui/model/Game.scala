package com.goexp.galgame.gui.model

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.StringOption
import com.goexp.galgame.common.model.game.{CommonGame, GameLocation, GameState}
import javafx.beans.property.{SimpleIntegerProperty, SimpleObjectProperty}

import java.util.StringJoiner
import scala.beans.BeanProperty

class Game extends CommonGame {
  @BeanProperty lazy val state = new SimpleObjectProperty[GameState]
  @BeanProperty lazy val location = new SimpleObjectProperty[GameLocation]
  @BeanProperty lazy val star = new SimpleIntegerProperty()

  @BeanProperty var brand: Brand = _

  def getWriter: String = String.join(",", writer)


  def getType: String = String.join(",", `type`)

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