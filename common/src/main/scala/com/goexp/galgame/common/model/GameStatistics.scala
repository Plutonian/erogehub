package com.goexp.galgame.common.model

import java.time.LocalDate

case class GameStatistics(start: LocalDate,
                          end: LocalDate,
                          count: Int,
                          realCount: Int,
                          state: StateStatistics,
                          star: StarStatistics)

case class StateStatistics(played: Int,
                           playing: Int,
                           hope: Int,
                           viewLater: Int,
                           uncheck: Int)

case class StarStatistics(zero: Int,
                          one: Int,
                          two: Int,
                          three: Int,
                          fore: Int,
                          five: Int)
