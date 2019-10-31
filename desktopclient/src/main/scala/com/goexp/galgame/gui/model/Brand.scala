package com.goexp.galgame.gui.model

import java.time.LocalDate
import java.util.StringJoiner

import com.goexp.galgame.common.model.game.brand.CommonBrand

import scala.beans.BeanProperty

class Brand extends CommonBrand {
  @BeanProperty
  var start:LocalDate=_
  @BeanProperty
  var end:LocalDate=_
  @BeanProperty
  var size:Int=_

  override def toString: String =
    new StringJoiner(", ", classOf[Brand].getSimpleName + "[", "]")
      .add("id=" + id)
      .add("name='" + name + "'")
      .add("comp='" + comp + "'")
      .add("isLike=" + isLike)
      .add("website='" + website + "'")
      //      .add("start=" + start)
      //      .add("end=" + end)
      //      .add("size=" + size)
      .toString

}