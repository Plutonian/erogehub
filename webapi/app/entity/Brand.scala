package entity

import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.brand.CommonBrand
import com.goexp.galgame.common.model.game.GameStatistics
import com.goexp.galgame.data.model.Game

import java.util
import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._



class Brand extends CommonBrand {

  @BeanProperty
  var tag: List[String] = _
  var statistics: GameStatistics = _

  @BeanProperty
  var emotion: Emotion = _

  @BeanProperty
  var series: util.List[Series] = _


  def getJavaTag() = {
    tag.asJava
  }


  override def toString = s"Brand($tag, $statistics, $emotion, $series)"
}