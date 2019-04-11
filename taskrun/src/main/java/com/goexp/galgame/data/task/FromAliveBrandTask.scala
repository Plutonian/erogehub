package com.goexp.galgame.data.task

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Piplline
import com.goexp.galgame.data.task.handler.game.{Bytes2Html, Html2GameOK, LocalGameHandler, ProcessGameOK}
import com.goexp.galgame.data.task.handler.starter.FromAllBrand
import com.goexp.galgame.data.task.handler.{DownloadBrandHandler, DownloadGameHandler, MesType, ProcessGameList}

object FromAliveBrandTask {
  def main(args: Array[String]): Unit = {
    Network.initProxy()

    new Piplline(new FromAllBrand)
      .registryIOTypeMessageHandler(99, new DownloadBrandHandler)
      .registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList)
      .registryIOTypeMessageHandler(MesType.NEED_DOWN_GAME, new DownloadGameHandler)
      .registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler)
      .registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html)
      .registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK)
      .registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK)
      .start()
  }
}