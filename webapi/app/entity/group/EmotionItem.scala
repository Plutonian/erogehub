package entity.group

import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.data.model.Game

import scala.beans.BeanProperty

case class EmotionItem(
                        @BeanProperty title: String,
                        @BeanProperty count: Int,
                        @BeanProperty emotion: Emotion,
                        @BeanProperty games: Array[Game]
                      ) extends DataItem
