package com.goexp.galgame.data.script.source.erogamescape

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.erogamescape.task.game.{GetGameBasic, Html2GameBasic, SaveGameBasic}
import com.goexp.galgame.data.source.erogamescape.task.starter.FromYear
import com.goexp.galgame.data.source.erogamescape.task.{GetGameList, PreProcessGame}
import com.goexp.piplline.core.Pipeline

object FromYearTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    //    val range = Range.inclusive(1990, LocalDate.now.getYear)
    val range = Range.inclusive(2000, 2000)

    new Pipeline(new FromYear(range))
      .regForIOType(new GetGameList)
      .regForCPUType(new PreProcessGame)
      .regForIOType(new GetGameBasic)
      .regForCPUType(new Html2GameBasic)
      .regForIOType(new SaveGameBasic)
      .start()
  }
}