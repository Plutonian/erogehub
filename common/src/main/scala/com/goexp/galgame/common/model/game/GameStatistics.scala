package com.goexp.galgame.common.model.game

import java.time.LocalDate
import scala.beans.BeanProperty

case class GameStatistics(
                           @BeanProperty start: LocalDate,
                           @BeanProperty end: LocalDate,
                           @BeanProperty count: Int,
                           @BeanProperty realCount: Int,
                           @BeanProperty state: StateStatistics,
                           @BeanProperty star: StarStatistics,
                           @BeanProperty location: LocationStatistics
                         )


case class StateStatistics(played: Int,
                           playing: Int,
                           hope: Int,
                           //                           viewLater: Int,
                           uncheck: Int)


case class StarStatistics(zero: Int,
                          one: Int,
                          two: Int,
                          three: Int,
                          four: Int,
                          five: Int)


case class LocationStatistics(local: Int,
                              //                              netdisk: Int,
                              remote: Int)

