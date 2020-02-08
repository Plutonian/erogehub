package com.goexp.galgame.common.model.game

import com.goexp.common.util.string.Strings

case class GameCharacter(name: String,
                         cv: String,
                         intro: String,
                         trueCV: String,
                         img: String,
                         index: Int) {


  override def toString = s"GameCharacter([$index]$name, $cv, $trueCV, $img)"

  def getShowCV(): Option[String] = {
    if (Strings.isNotEmpty(trueCV)) Some(s"*$trueCV")
    else Some(cv).filter(Strings.isNotEmpty)
  }
}