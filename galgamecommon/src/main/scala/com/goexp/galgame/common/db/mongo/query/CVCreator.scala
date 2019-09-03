package com.goexp.galgame.common.db.mongo.query

import java.util.List

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.CV
import org.bson.Document
import org.slf4j.LoggerFactory

object CVCreator extends ObjectCreator[CV] {
  private val logger = LoggerFactory.getLogger(CVCreator.getClass)

  override def create(doc: Document): CV = {
    logger.debug("<create> doc={}", doc)

    val cv = new CV
    cv.id = doc.getInteger("_id")
    cv.name = doc.getString("name")
    cv.star = doc.getInteger("star")

    cv.start = Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull
    cv.end = Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull
    cv.size = doc.getInteger("size")

    cv.tag = doc.get("tag", classOf[List[String]])
    cv.nameStr = doc.getString("names")
    cv
  }
}