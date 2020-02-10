package com.goexp.galgame.common.model.game

case class GameImg(src: String,
                   index: Int) {

  override def toString = s"GameImg([$index],$src)"
}