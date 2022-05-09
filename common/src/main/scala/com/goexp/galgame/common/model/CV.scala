package com.goexp.galgame.common.model

import java.time.LocalDate
import java.util.StringJoiner

import scala.beans.BeanProperty

class CV {

  var id: Int = _

  @BeanProperty
  var name: String = _
  var nameStr: String = _

  @BeanProperty
  var star: Int = _

  @BeanProperty
  var trueName: String = _

  @BeanProperty
  var start: LocalDate = _
  @BeanProperty
  var end: LocalDate = _
  @BeanProperty
  var size: Int = _

  @BeanProperty
  var tag: List[String] = _

  @BeanProperty
  var statistics: GameStatistics = _


  override def toString: String =
    new StringJoiner(", ", classOf[CV].getSimpleName + "[", "]")
      .add("name='" + name + "'")
      .add("trueName='" + trueName + "'")
      .add("star=" + star)
      .add("nameStr='" + nameStr + "'")
      .toString
}