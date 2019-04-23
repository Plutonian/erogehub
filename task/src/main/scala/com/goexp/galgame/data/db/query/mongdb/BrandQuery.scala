package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.data.model.Brand
import org.bson.Document
import org.slf4j.LoggerFactory

object BrandQuery {
  lazy val tlp = new DBQueryTemplate.Builder[Brand](DB_NAME, "brand", new BrandCreator).build

  private class BrandCreator extends ObjectCreator[Brand] {
    private val logger = LoggerFactory.getLogger(classOf[BrandCreator])

    override def create(doc: Document): Brand = {
      logger.debug("<create> doc={}", doc)

      val parentCreator = new CommonBrandCreator(new Brand)
      parentCreator.create(doc).asInstanceOf[Brand]
    }
  }

}