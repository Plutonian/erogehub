package com.goexp.galgame.common.model

import java.util.StringJoiner

import scala.beans.BeanProperty

abstract class CommonBrand {
  @BeanProperty
  var id = 0
  @BeanProperty
  var name: String = _
  @BeanProperty
  var website: String = _
  @BeanProperty
  var comp: String = _
  @BeanProperty
  var isLike: BrandType = _



  override def toString: String = new StringJoiner(", ", classOf[CommonBrand].getSimpleName + "[", "]").add("id=" + id).add("name='" + name + "'").add("website='" + website + "'").add("comp='" + comp + "'").toString

  def canEqual(other: Any): Boolean = other.isInstanceOf[CommonBrand]

  override def equals(other: Any): Boolean = other match {
    case that: CommonBrand =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}