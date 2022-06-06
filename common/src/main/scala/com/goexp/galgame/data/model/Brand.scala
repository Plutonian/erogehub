package com.goexp.galgame.data.model

import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.brand.CommonBrand
import com.goexp.galgame.common.model.game.GameStatistics

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._

class Brand extends CommonBrand {

  @BeanProperty
  var tag: List[String] = _
  var statistics: GameStatistics = _

  @BeanProperty
  var state: Emotion = _


  def getJavaTag() = {
    tag.asJava
  }
}