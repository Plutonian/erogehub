package com.goexp.galgame.data.source.getchu.importor

import java.util

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.model.{GameStatistics, LocationStatistics, StarStatistics, StateStatistics}
import com.goexp.galgame.data.model.Brand
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

object BrandDB {

  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "brand")

  def insert(item: Brand) = {
    val doc = new Document("_id", item.id)
      .append("name", item.name)
      .append("website", item.website)
      .append("type", 0)

    tlp.exec(gameC => {
      gameC.insertOne(doc)
    })
  }

  def updateWebsite(item: Brand) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), set("website", item.website))
    })

  def updateComp(item: Brand) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), set("comp", item.comp))
    })


  def updateStatistics(item: Brand, tag: util.List[String], statistics: GameStatistics) = {

    val GameStatistics(start, end, count, realCount,
    StateStatistics(played, playing, hope, viewLater, uncheck),
    StarStatistics(zero, one, two, three, four, five),
    LocationStatistics(local, netdisk, remote)) = statistics

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), combine(
        set("tag", tag),
        set("statistics.start", start),
        set("statistics.end", end),
        set("statistics.count", count),
        set("statistics.realCount", realCount),

        set("statistics.state.played", played),
        set("statistics.state.playing", playing),
        set("statistics.state.hope", hope),
        set("statistics.state.viewLater", viewLater),
        set("statistics.state.uncheck", uncheck),

        set("statistics.star.zero", zero),
        set("statistics.star.one", one),
        set("statistics.star.two", two),
        set("statistics.star.three", three),
        set("statistics.star.four", four),
        set("statistics.star.five", five),

        set("statistics.location.local", local),
        set("statistics.location.netdisk", netdisk),
        set("statistics.location.remote", remote)
      ))
    })
  }
}