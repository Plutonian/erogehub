package com.goexp.galgame.gui.model

import java.time.LocalDate
import java.util.StringJoiner

import com.goexp.galgame.common.model.GameStatistics
import com.goexp.galgame.common.model.game.brand.CommonBrand

class Brand extends CommonBrand {
  //  var start: LocalDate = _
  //  var end: LocalDate = _
  //  var size: Int = _
  var tag: List[String] = _
  var statistics: GameStatistics = _


  override def toString: String =
    new StringJoiner(", ", classOf[Brand].getSimpleName + "[", "]")
      .add("id=" + id)
      .add("name='" + name + "'")
      .add("comp='" + comp + "'")
      .add("state=" + state)
      .add("website='" + website + "'")
      //      .add("start=" + start)
      //      .add("end=" + end)
      //      .add("size=" + size)
      .toString

}