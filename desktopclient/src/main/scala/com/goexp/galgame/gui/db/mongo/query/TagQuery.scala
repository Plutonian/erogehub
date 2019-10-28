package com.goexp.galgame.gui.db.mongo.query

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.goexp.galgame.common.model.TagType
import org.bson.Document
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object TagQuery {
  private val logger = LoggerFactory.getLogger(TagQuery.getClass)
  private val creator: ObjectCreator[TagType] = (doc: Document) => {
    val t = doc.getString("type")
    val o = doc.getInteger("order")
    val tags = Option(doc.get("tags", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    TagType(t, o, tags)
  }
  val tlp = new DBQueryTemplate.Builder[TagType](DB_NAME, "tag", creator).build
}
