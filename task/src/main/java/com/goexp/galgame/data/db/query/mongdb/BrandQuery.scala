package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.data.model.Brand
import org.bson.Document
import org.slf4j.LoggerFactory

object BrandQuery {
  var tlp = new DBQueryTemplate.Builder[Brand]("galgame", "brand", new BrandCreator).build

  private class BrandCreator extends ObjectCreator[Brand] {
    private val logger = LoggerFactory.getLogger(classOf[BrandCreator])

    override def create(doc: Document): Brand = {
      logger.debug("<create> doc={}", doc)

      val g = new Brand
      g.id = doc.getInteger("_id")
      g.name = doc.getString("name")
      g.website = doc.getString("website")
      g.comp = doc.getString("comp")
      g
    }
  }

}