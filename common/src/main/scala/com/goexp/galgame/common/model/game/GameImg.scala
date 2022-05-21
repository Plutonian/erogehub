package com.goexp.galgame.common.model.game

import scala.beans.BeanProperty

case class GameImg(
                    src: String,
                    @BeanProperty index: Int
                  ) {

  override def toString = s"GameImg([$index],$src)"
}