package entity.group

import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.data.model.Game

import scala.beans.BeanProperty

case class LocationItem(
                         @BeanProperty title: String,
                         @BeanProperty count: Int,
                         @BeanProperty location: GameLocation,
                         @BeanProperty games: Array[Game]
                       ) extends DataItem
