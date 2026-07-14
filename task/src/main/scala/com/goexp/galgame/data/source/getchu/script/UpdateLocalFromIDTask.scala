package com.goexp.galgame.data.source.getchu.script

import com.goexp.galgame.common.util.{DateUtil, Network}
import com.goexp.galgame.data.source.getchu.actor.{ActorConfig, DownloadPageActor}
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.goexp.pipeline.core.Pipeline
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.*
import com.typesafe.scalalogging.Logger

import java.time.LocalDate


object UpdateLocalFromIDTask {

  private val logger = Logger(UpdateLocalFromDateRangeTask.getClass)

  def main(args: Array[String]): Unit = {


    Network.initProxy()

    val id=1343044

    val game = GameFullQuery().where(Filters.eq(id)).one()

    game.foreach{g=>{

      val pipeline = new Pipeline()
        .regGroup(ActorConfig.DefaultGameProcessGroup)
        .start()

      pipeline.sendTo[DownloadPageActor](g.id)

    }}




  }
}
