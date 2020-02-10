package com.goexp.galgame.common.model.game.guide

class GameGuide {
  var id: String = _
  var title: String = _
  var href: String = _
  var html: String = _
  var from: DataFrom = _


  def canEqual(other: Any): Boolean = other.isInstanceOf[GameGuide]

  override def equals(other: Any): Boolean = other match {
    case that: GameGuide =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}