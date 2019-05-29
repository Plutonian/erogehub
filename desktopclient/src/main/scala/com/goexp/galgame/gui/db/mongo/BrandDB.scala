package com.goexp.galgame.gui.db.mongo

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set
import com.goexp.galgame.common.db.mongo.DB_NAME


object BrandDB {

  private val tlp = new DBOperatorTemplate(DB_NAME, "brand")

  def updateWebsite(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("website", item.website))
    }

  def updateIsLike(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("type", item.isLike.value))
    }
}