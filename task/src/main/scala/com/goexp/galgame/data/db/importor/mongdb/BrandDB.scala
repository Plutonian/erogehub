package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.data.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set
import org.bson.Document

object BrandDB {
  lazy val tlp = new DBOperatorTemplate("galgame", "brand")
}

class BrandDB {
  def insert(item: Brand) = {
    val doc = new Document("_id", item.id)
      .append("name", item.name)
      .append("website", item.website)
      .append("type", 0)

    BrandDB.tlp.exec(gameC => {
      gameC.insertOne(doc)
    })
  }

  def updateWebsite(item: Brand) =
    BrandDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), set("website", item.website))
    })

  def updateComp(item: Brand) =
    BrandDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), set("comp", item.comp))
    })
}