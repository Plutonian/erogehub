package com.goexp.galgame.common.model.game

import java.time.LocalDate
import scala.beans.BeanProperty

case class GameStatistics(
                           @BeanProperty start: LocalDate,
                           @BeanProperty end: LocalDate,
                           @BeanProperty count: Int,
                           @BeanProperty realCount: Int,
                           @BeanProperty emotion: EmotionStatistics,
                           @BeanProperty star: StarStatistics,
                           @BeanProperty location: LocationStatistics
                         )


case class EmotionStatistics(
                            @BeanProperty like: Int,
                            @BeanProperty hope: Int,
                            @BeanProperty normal: Int,
                            @BeanProperty hate: Int
                          )


case class StarStatistics(
                           @BeanProperty zero: Int,
                           @BeanProperty one: Int,
                           @BeanProperty two: Int,
                           @BeanProperty three: Int,
                           @BeanProperty four: Int,
                           @BeanProperty five: Int
                         )


case class LocationStatistics(
                               @BeanProperty local: Int,
                               @BeanProperty remote: Int
                             )

