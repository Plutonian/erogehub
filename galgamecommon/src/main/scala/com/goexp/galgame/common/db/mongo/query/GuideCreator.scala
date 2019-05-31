package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.common.model.CommonGame.Guide
import org.bson.Document
import org.slf4j.LoggerFactory

object GuideCreator extends ObjectCreator[CommonGame.Guide] {
  private val logger = LoggerFactory.getLogger(GuideCreator.getClass)

  override def create(doc: Document): CommonGame.Guide = {
    logger.debug("<create> doc={}", doc)

    val guide = new CommonGame.Guide
    guide.id = doc.getString("_id")
    guide.title = doc.getString("title")
    guide.href = doc.getString("href")
    guide.html = doc.getString("html")
    guide.from = Guide.DataFrom.from(doc.getInteger("from"))
    guide
  }
}