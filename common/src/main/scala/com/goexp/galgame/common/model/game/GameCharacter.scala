package com.goexp.galgame.common.model.game

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.CV

import scala.beans.BeanProperty

case class GameCharacter(@BeanProperty name: String,
                         @BeanProperty cv: String,
                         @BeanProperty intro: String,
                         @BeanProperty trueCV: String,
                         @BeanProperty img: String,
                         @BeanProperty index: Int
                        ) {
  @BeanProperty
  var cvObj: CV = _

  var man: Boolean = _


  @BeanProperty
  def isHeroine() = {
    !man && (Strings.isNotEmpty(cv) || Strings.isNotEmpty(img))
  }


  override def toString = s"GameCharacter([$index]$name, $cv, $trueCV, $img)"

  //  def getShowCV(): Option[String] = {
  //    if (Strings.isNotEmpty(trueCV)) Some(s"*$trueCV")
  //    else Some(cv).filter(Strings.isNotEmpty)
  //  }
}