package com.goexp.galgame.data.task

import java.time.LocalDate

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Piplline
import com.goexp.galgame.data.task.handler.game.DefaultGameProcessGroup
import com.goexp.galgame.data.task.handler.starter.FromDateRange
import com.goexp.galgame.data.task.handler.{DownloadGameHandler, MesType, PreProcessGame}

object FromDateRangeTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    val start = LocalDate.now.minusMonths(1).withDayOfMonth(1)
    val end = LocalDate.now.withMonth(12).withDayOfMonth(31)

//    val configs = new HandlerConfigGroup(
//      new HandlerConfig(MesType.Game, new LocalGameHandler, 2),
//      new HandlerConfig(MesType.ContentBytes, new Bytes2Html, 30),
//      new HandlerConfig(MesType.ContentHtml, new Html2GameOK, 30),
//      new HandlerConfig(MesType.GAME_OK, new ProcessGameOK, 30)
//    )

    new Piplline(new FromDateRange(start, end))
      .regForCPUType(MesType.PRE_GAME, new PreProcessGame)
      .regForIOType(MesType.NEED_DOWN_GAME, new DownloadGameHandler)
      .regGroup(DefaultGameProcessGroup)
      //      .regForIOType(MesType.Game, new LocalGameHandler,2)
      //      .regForCPUType(MesType.ContentBytes, new Bytes2Html)
      //      .regForCPUType(MesType.ContentHtml, new Html2GameOK)
      //      .regForCPUType(MesType.GAME_OK, new ProcessGameOK)
      .start()
  }
}