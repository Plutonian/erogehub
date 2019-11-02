package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.game.guide.{DataFrom, GameGuide}
import org.bson.Document
import com.typesafe.scalalogging.Logger

object GuideCreator extends ObjectCreator[GameGuide] {
  private val logger = Logger(GuideCreator.getClass)

  override def create(doc: Document): GameGuide = {
    logger.trace(s"<Doc> $doc")

    val guide = new GameGuide
    guide.id = doc.getString("_id")
    guide.title = doc.getString("title")
    guide.href = doc.getString("href")
    guide.html = doc.getString("html")
    guide.from = DataFrom.from(doc.getInteger("from"))

    logger.trace(s"<guide> $guide")

    guide
  }
}