package com.goexp.galgame.data.source.getchu.importor

import java.time.LocalDate

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.goexp.galgame.data.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

object BrandDB {
  val tlp = new DBOperatorTemplate(DB_NAME, "brand")

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


  def updateStatistics(item: Brand, start: LocalDate, end: LocalDate, size: Int) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), combine(
        set("start", start),
        set("end", end),
        set("size", size)
      ))
    })
}