package com.goexp.galgame.data.script.source.erogamescape

import java.time.LocalDate

import com.goexp.galgame.data.source.erogamescape.task.starter.FromYear
import com.goexp.galgame.data.source.erogamescape.task.{GetGameList, PreProcessGame}
import com.goexp.piplline.core.Pipeline

object FromDateRangeTask {
  def main(args: Array[String]) = {
    //    Network.initProxy()

    val range = Range.inclusive(1990, LocalDate.now.getYear)

    new Pipeline(new FromYear(range))
      .regForIOType(new GetGameList)
      .regForCPUType(new PreProcessGame)
      //      .regGroup(DefaultGameProcessGroup)
      .start()
  }
}