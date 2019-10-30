package com.goexp.galgame.data.script.source.erogamescape

import java.time.LocalDate

import com.goexp.galgame.data.source.erogamescape.task.game.{GetGameBasic, Html2GameBasic, SaveGameBasic}
import com.goexp.galgame.data.source.erogamescape.task.starter.FromYear
import com.goexp.galgame.data.source.erogamescape.task.{GetGameList, PreProcessGame}
import com.goexp.piplline.core.Pipeline

object FromYearTask {
  def main(args: Array[String]) = {

    val range = Range.inclusive(LocalDate.now.getYear, LocalDate.now.getYear)

    new Pipeline(new FromYear(range))
      .regForIOType(new GetGameList)
      .regForCPUType(new PreProcessGame)
      .regForIOType(new GetGameBasic)
      .regForCPUType(new Html2GameBasic)
      .regForIOType(new SaveGameBasic)
      .start()
  }
}