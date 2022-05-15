package com.goexp.galgame.gui.model

import java.util.StringJoiner
import com.goexp.galgame.common.model.GameStatistics
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.model.game.brand.{BrandState, CommonBrand}
import javafx.beans.property.SimpleObjectProperty

import scala.beans.BeanProperty

class Brand extends CommonBrand {
  var tag: List[String] = _
  var statistics: GameStatistics = _

  @BeanProperty
  lazy val state = new SimpleObjectProperty[BrandState]


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