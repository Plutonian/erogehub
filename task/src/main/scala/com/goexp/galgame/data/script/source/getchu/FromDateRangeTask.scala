package com.goexp.galgame.data.script.source.getchu

import java.time.LocalDate

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.task.handler.DefaultGameProcessGroup
import com.goexp.galgame.data.source.getchu.task.handler.starter.FromDateRange
import com.goexp.piplline.core.Pipeline

object FromDateRangeTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    val start = LocalDate.now.minusMonths(1).withDayOfMonth(1)

    val end = start.plusMonths(6)
    //    val end = LocalDate.now.withMonth(12).withDayOfMonth(31)


    new Pipeline(new FromDateRange(start, end))
      .regGroup(DefaultGameProcessGroup)
      .start()
  }
}