package com.goexp.galgame.data.source.getchu.script

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.source.getchu.actor.{ActorConfig, DownloadPageActor}
import com.goexp.pipeline.core.Pipeline

import java.time.LocalDate

object FromDateRangeTask {
  def main(args: Array[String]) = {
    Network.initProxy()

    args.length match {
      case 0 =>
        val start = LocalDate.now.withDayOfMonth(1)

        val end = start.plusMonths(6)

        val pipeline = new Pipeline()
          .regGroup(ActorConfig.DefaultGameProcessGroup)
          .start()

        pipeline.sendTo[DownloadPageActor]((start, end))

      case 2 =>
        val start = LocalDate.parse(args(0))
        val end = LocalDate.parse(args(1))

        val pipeline = new Pipeline()
          .regGroup(ActorConfig.DefaultGameProcessGroup)
          .start()

        pipeline.sendTo[DownloadPageActor]((start, end))
      case _ =>
        println("<start> <end>")
    }


  }
}
