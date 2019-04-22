package com.goexp.galgame.gui.db.mongo

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set

object BrandDB {
  private val tlp = new DBOperatorTemplate("galgame", "brand")

  def updateWebsite(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("website", item.website))
    }

  def updateIsLike(item: Brand) =
    tlp.exec { documentMongoCollection =>
      documentMongoCollection.updateOne(Filters.eq(item.id), set("type", item.isLike.getValue))
    }
}