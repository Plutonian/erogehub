package com.goexp.galgame.common.model.game

case class GameCharacter(name: String,
                         cv: String,
                         intro: String,
                         trueCV: String,
                         img: String,
                         index: Int) {


  override def toString = s"GameCharacter([$index]$name, $cv, $trueCV, $img)"
}