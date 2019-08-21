package com.goexp.galgame.common.model

import java.util.StringJoiner

import scala.beans.BeanProperty

class CV {
  @BeanProperty
  var name: String = null
  var nameStr: String = null

  @BeanProperty
  var star = 0

  @BeanProperty
  var trueName: String = null


  override def toString: String = new StringJoiner(", ", classOf[CV].getSimpleName + "[", "]").add("name='" + name + "'").add("nameStr='" + nameStr + "'").add("star=" + star).add("trueName='" + trueName + "'").toString
}