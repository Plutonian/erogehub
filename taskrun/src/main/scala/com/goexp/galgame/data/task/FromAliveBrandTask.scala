package com.goexp.galgame.data.task

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.piplline.core.Piplline
import com.goexp.galgame.data.task.handler.game.DefaultGameProcessGroup
import com.goexp.galgame.data.task.handler.starter.FromAllBrand
import com.goexp.galgame.data.task.handler.{DownloadBrandHandler, DownloadGameHandler, MesType, ProcessGameList}

object FromAliveBrandTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    new Piplline(new FromAllBrand)
      .regForIOType(99, new DownloadBrandHandler)
      .regForCPUType(MesType.Brand, new ProcessGameList)
      .regForIOType(MesType.NEED_DOWN_GAME, new DownloadGameHandler)
      .regGroup(DefaultGameProcessGroup)
      .start()
  }
}