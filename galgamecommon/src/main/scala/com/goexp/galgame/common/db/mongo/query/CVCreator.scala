package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.CV
import org.bson.Document
import org.slf4j.LoggerFactory

object CVCreator extends ObjectCreator[CV] {
  private val logger = LoggerFactory.getLogger(CVCreator.getClass)

  override def create(doc: Document): CV = {
    logger.debug("<create> doc={}", doc)

    val g = new CV
    g.name = doc.getString("name")
    g.star = doc.getInteger("star")
    g.nameStr = doc.getString("names")
    g
  }
}