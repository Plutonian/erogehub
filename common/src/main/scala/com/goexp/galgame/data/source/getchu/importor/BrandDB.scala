package com.goexp.galgame.data.source.getchu.importor

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.game.{EmotionStatistics, GameStatistics, LocationStatistics, StarStatistics}
import com.goexp.galgame.data.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set, unset}
import org.bson.Document

import java.util

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
    EmotionStatistics(like, hope, normal, hate),
    StarStatistics(zero, one, two, three, four, five),
    LocationStatistics(local, remote)) = statistics

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), combine(
        unset("statistics")
      ))

      documentMongoCollection.updateOne(Filters.eq(item.id), combine(
        set("tag", tag),
        set("statistics.start", start),
        set("statistics.end", end),
        set("statistics.count", count),
        set("statistics.realCount", realCount),


        //        unset("statistics.emotion"),
        set("statistics.emotion.LIKE", like),
        set("statistics.emotion.HOPE", hope),
        set("statistics.emotion.NORMAL", normal),
        set("statistics.emotion.HATE", hate),

        //        unset("statistics.star"),
        set("statistics.star.zero", zero),
        set("statistics.star.one", one),
        set("statistics.star.two", two),
        set("statistics.star.three", three),
        set("statistics.star.four", four),
        set("statistics.star.five", five),

        //        unset("statistics.location"),
        set("statistics.location.local", local),
        set("statistics.location.remote", remote)
      ))
    })
  }
}