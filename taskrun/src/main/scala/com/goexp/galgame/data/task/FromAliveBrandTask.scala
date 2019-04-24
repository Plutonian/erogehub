package com.goexp.galgame.data.task

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Piplline
import com.goexp.galgame.data.task.handler.game.{Bytes2Html, Html2GameOK, LocalGameHandler, ProcessGameOK}
import com.goexp.galgame.data.task.handler.starter.FromAllBrand
import com.goexp.galgame.data.task.handler.{DownloadBrandHandler, DownloadGameHandler, MesType, ProcessGameList}

object FromAliveBrandTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    new Piplline(new FromAllBrand)
      .regForIOType(99, new DownloadBrandHandler)
      .regForCPUType(MesType.Brand, new ProcessGameList)
      .regForIOType(MesType.NEED_DOWN_GAME, new DownloadGameHandler)
      .regForCPUType(MesType.Game, new LocalGameHandler)
      .regForCPUType(MesType.ContentBytes, new Bytes2Html)
      .regForCPUType(MesType.ContentHtml, new Html2GameOK)
      .regForCPUType(MesType.GAME_OK, new ProcessGameOK)
      .start()
  }
}