package com.goexp.galgame.data.script.source.getchu

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.actor.{ActorConfig, DownloadPageActor}
import com.goexp.piplline.core.Pipeline

object FromDoujinBrandTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    if (args.length > 0) {

      args(0).toIntOption match {
        case Some(brandId) =>
          val pipeline = new Pipeline()
            .regGroup(ActorConfig.DefaultGameProcessGroup)
            .start()

          pipeline.sendTo[DownloadPageActor]((brandId, "doujin"))
        case None =>
          println("args must be number")
      }

    } else {
      println("<brandId>")
    }
  }
}