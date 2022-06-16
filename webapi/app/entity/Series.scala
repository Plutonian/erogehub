package entity

import com.goexp.galgame.data.model.Game

import java.util
import scala.beans.BeanProperty

case class Series(
                   @BeanProperty name: String,
                   @BeanProperty games: util.List[Game])
