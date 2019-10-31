package com.goexp.galgame.common.model.game

class GameCharacter {
  var name: String = _
  var cv: String = _
  var intro: String = _
  var trueCV: String = _
  var img: String = _
  var index: Int = _


  override def toString = s"GameCharacter([$index]$name, $cv, $trueCV, $img)"
}