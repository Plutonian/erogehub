package com.goexp.galgame.common.db.mongo.query

import java.util

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.CV
import org.bson.Document
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object CVCreator extends ObjectCreator[CV] {
  private val logger = Logger(CVCreator.getClass)

  override def create(doc: Document): CV = {
    logger.debug("<create> doc={}", doc)

    val cv = new CV
    cv.id = doc.getInteger("_id")
    cv.name = doc.getString("name")
    cv.star = doc.getInteger("star")

    cv.start = Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull
    cv.end = Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull
    cv.size = doc.getInteger("size")

    cv.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    cv.nameStr = doc.getString("names")
    cv
  }
}