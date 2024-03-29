package com.goexp.galgame.common.db.mongo.query

import java.util

import com.goexp.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.CV
import com.typesafe.scalalogging.Logger
import org.bson.Document

import scala.jdk.CollectionConverters._

object CVCreator extends ObjectCreator[CV] {

  private val logger = Logger(CVCreator.getClass)


  override def create(doc: Document): CV = {
    logger.trace(s"<Doc> $doc")

    val cv = new CV
    cv.id = doc.getInteger("_id")
    cv.name = doc.getString("name")
    cv.star = doc.getInteger("star")

    cv.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    cv.nameStr = doc.getString("names")

    logger.trace(s"<CV> $cv")

    cv
  }
}