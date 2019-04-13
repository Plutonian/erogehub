package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.model.CV
import org.bson.Document
import org.slf4j.LoggerFactory

object CVQuery {
  val tlp = new DBQueryTemplate.Builder[CV]("galgame", "cv", new CVCreator).build

  private class CVCreator extends ObjectCreator[CV] {
    private val logger = LoggerFactory.getLogger(classOf[CVCreator])

    override def create(doc: Document): CV = {
      logger.debug("<create> doc={}", doc)

      val g = new CV
      g.name = doc.getString("name")
      g.star = doc.getInteger("star")
      g.nameStr = doc.getString("names")
      g
    }
  }

}