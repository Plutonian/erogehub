package com.goexp.galgame.gui.db.mongo

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set


object BrandDB {

  private val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "brand")

  def updateWebsite(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("website", item.website))
    }

  def updateIsLike(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("type", item.state.value))
    }
}