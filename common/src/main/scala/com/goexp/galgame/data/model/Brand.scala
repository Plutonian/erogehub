package com.goexp.galgame.data.model

import com.goexp.galgame.common.model.brand.{BrandState, CommonBrand}
import com.goexp.galgame.common.model.game.GameStatistics

import scala.beans.BeanProperty

class Brand extends CommonBrand {

  var tag: List[String] = _
  var statistics: GameStatistics = _

  @BeanProperty
  var state: BrandState = _
}