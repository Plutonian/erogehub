package com.goexp.galgame.common.model.game

import com.goexp.common.util.string.Strings

import scala.beans.BeanProperty

case class GameCharacter(@BeanProperty name: String,
                         @BeanProperty cv: String,
                         @BeanProperty intro: String,
                         @BeanProperty trueCV: String,
                         @BeanProperty img: String,
                         @BeanProperty index: Int) {


  override def toString = s"GameCharacter([$index]$name, $cv, $trueCV, $img)"

  def getShowCV(): Option[String] = {
    if (Strings.isNotEmpty(trueCV)) Some(s"*$trueCV")
    else Some(cv).filter(Strings.isNotEmpty)
  }
}