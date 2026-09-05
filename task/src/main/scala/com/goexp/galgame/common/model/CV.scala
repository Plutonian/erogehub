package com.goexp.galgame.common.model

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameStatistics

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
  var tag: List[String] = _

  @BeanProperty
  var statistics: GameStatistics = _

//  def isReal() = {
//    Strings.isNotEmpty(trueName)
//  }


  override def toString: String =
    new StringJoiner(", ", classOf[CV].getSimpleName + "[", "]")
      .add("name='" + name + "'")
      .add("trueName='" + trueName + "'")
      .add("star=" + star)
      .add("nameStr='" + nameStr + "'")
      .toString
}