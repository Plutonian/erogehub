package com.goexp.galgame.data.task

import java.time.LocalDate

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Piplline
import com.goexp.galgame.data.task.handler.game.{Bytes2Html, Html2GameOK, LocalGameHandler, ProcessGameOK}
import com.goexp.galgame.data.task.handler.starter.FromDateRange
import com.goexp.galgame.data.task.handler.{DownloadGameHandler, MesType, PreProcessGame}

object FromDateRangeTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    val start = LocalDate.now.minusMonths(1).withDayOfMonth(1)
    val end = LocalDate.now.withMonth(12).withDayOfMonth(31)

    new Piplline(new FromDateRange(start, end))
      .registryCPUTypeMessageHandler(MesType.PRE_GAME, new PreProcessGame)
      .registryIOTypeMessageHandler(MesType.NEED_DOWN_GAME, new DownloadGameHandler)
      .registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler)
      .registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html)
      .registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK)
      .registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK)
      .start()
  }
}