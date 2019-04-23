package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.model.CommonGame
import org.bson.Document
import org.slf4j.LoggerFactory

object GuideQuery {
  lazy val tlp = new DBQueryTemplate.Builder[CommonGame.Guide](DB_NAME, "guide", new Creator).build

  private class Creator extends ObjectCreator[CommonGame.Guide] {
    private lazy val logger = LoggerFactory.getLogger(classOf[Creator])

    override def create(doc: Document): CommonGame.Guide = {
      logger.debug("<create> doc={}", doc)

      val g = new CommonGame.Guide
      g.id = doc.getString("_id")
      g.href = doc.getString("name")
      g.title = doc.getString("title")
      g.from = CommonGame.Guide.DataFrom.from(doc.getInteger("from"))
      g
    }
  }

}