package com.goexp.galgame.common.model

import java.util
import java.util.StringJoiner

import scala.beans.BeanProperty

class CV {
  @BeanProperty
  var name: String = _
  var nameStr: String = _

  @BeanProperty
  var star:Int = _

  @BeanProperty
  var trueName: String = _

  @BeanProperty
  var tag: util.List[String] = _


  override def toString: String =
    new StringJoiner(", ", classOf[CV].getSimpleName + "[", "]")
      .add("name='" + name + "'")
      .add("nameStr='" + nameStr + "'")
      .add("star=" + star)
      .add("trueName='" + trueName + "'")
      .toString
}