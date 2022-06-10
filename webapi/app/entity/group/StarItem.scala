package entity.group

import com.goexp.galgame.data.model.Game

import scala.beans.BeanProperty

case class StarItem(
                     @BeanProperty title: String,
                     @BeanProperty count: Int,
                     @BeanProperty star: Int,
                     @BeanProperty games: Array[Game]
                   ) extends DataItem
