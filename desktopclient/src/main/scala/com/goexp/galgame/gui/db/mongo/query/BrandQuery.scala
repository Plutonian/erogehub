package com.goexp.galgame.gui.db.mongo.query

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Sorts.ascending
import com.typesafe.scalalogging.Logger
import org.bson.Document

import scala.jdk.CollectionConverters._


object BrandQuery {
  private val logger = Logger(BrandQuery.getClass)

  private val creator: ObjectCreator[Brand] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val parentCreator = new CommonBrandCreator(new Brand)
    val b = parentCreator.create(doc).asInstanceOf[Brand]
    //      b.start=doc.getDate("start").
    b.start = Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull
    b.end = Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull
    b.size = doc.getInteger("size")
    b.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull

    logger.trace(s"<brand> ${b}")

    b

  }
  val tlp = new DBQueryTemplate.Builder[Brand](DB_NAME, "brand", creator).defaultSort(ascending("comp")).build

}
