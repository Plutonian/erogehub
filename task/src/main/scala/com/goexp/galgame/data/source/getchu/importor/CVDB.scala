package com.goexp.galgame.data.source.getchu.importor

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.model._
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}

object CVDB {
  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "cv")

  def updateStatistics(cv: CV, statistics: GameStatistics) = {

    val GameStatistics(start, end, count, realCount,
    StateStatistics(played, playing, hope, uncheck),
    StarStatistics(zero, one, two, three, four, five),
    LocationStatistics(local, remote)) = statistics

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(cv.id), combine(
        set("statistics.start", start),
        set("statistics.end", end),
        set("statistics.count", count),
        set("statistics.realCount", realCount),

        set("statistics.state.played", played),
        set("statistics.state.playing", playing),
        set("statistics.state.hope", hope),
        set("statistics.state.uncheck", uncheck),

        set("statistics.star.zero", zero),
        set("statistics.star.one", one),
        set("statistics.star.two", two),
        set("statistics.star.three", three),
        set("statistics.star.four", four),
        set("statistics.star.five", five),

        set("statistics.location.local", local),
        set("statistics.location.remote", remote)
      ))
    })
  }
}
