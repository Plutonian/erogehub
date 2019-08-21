package com.goexp.galgame.gui.model

import java.time.LocalDate

import com.goexp.galgame.common.model.CommonBrand
import java.util.StringJoiner

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
      .add("website='" + website + "'")
      .add("comp='" + comp + "'")
      .add("isLike=" + isLike)
      .add("start=" + start)
      .add("end=" + end)
      .add("size=" + size)
      .toString

  //    @Override
  //    public String toString() {
  //        return super.toString() + "Brand{" +
  //                "isLike=" + isLike +
  //                "} ";
  //    }
}