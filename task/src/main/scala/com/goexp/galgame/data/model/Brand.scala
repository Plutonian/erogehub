package com.goexp.galgame.data.model

import com.goexp.galgame.common.model.game.brand.{BrandState, CommonBrand}

import scala.beans.BeanProperty

class Brand extends CommonBrand {

  @BeanProperty
  var state: BrandState = _
}