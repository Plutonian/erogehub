package com.goexp.galgame.data.script.source.getchu

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.task.handler.DefaultGameProcessGroup
import com.goexp.galgame.data.source.getchu.task.handler.starter.FromBrand
import com.goexp.piplline.core.Pipeline

object FromDoujinBrandTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    if (args.length > 0) {

      args(0).toIntOption match {
        case Some(brandId) =>
          new Pipeline(new FromBrand(brandId))
            .regGroup(DefaultGameProcessGroup)
            .start()
        case None =>
          println("args must be number")
      }

    } else {
      println("<brandId>")
    }
  }
}