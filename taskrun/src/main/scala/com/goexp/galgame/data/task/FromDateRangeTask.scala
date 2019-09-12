package com.goexp.galgame.data.task

import java.time.LocalDate

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Pipeline
import com.goexp.galgame.data.task.handler.game.DefaultGameProcessGroup
import com.goexp.galgame.data.task.handler.starter.FromDateRange
import com.goexp.galgame.data.task.handler.{DownloadGameHandler, PreProcessGame}

object FromDateRangeTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    val start = LocalDate.now.minusMonths(1).withDayOfMonth(1)
    val end = LocalDate.now.withMonth(12).withDayOfMonth(31)


    new Pipeline(new FromDateRange(start, end))
      .regForCPUType(new PreProcessGame)
      .regForIOType(new DownloadGameHandler)
      .regGroup(DefaultGameProcessGroup)
      .start()
  }
}