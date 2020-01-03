package com.goexp.galgame.gui.db.mongo.query

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.model.TagType
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.typesafe.scalalogging.Logger
import org.bson.Document

import scala.jdk.CollectionConverters._


object TagQuery {
  private val logger = Logger(TagQuery.getClass)
  private val creator: ObjectCreator[TagType] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val tagType = TagType(
      doc.getString("type"),
      doc.getInteger("order"),
      Option(doc.get("tags", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    )

    logger.trace(s"<tagType> $tagType")

    tagType
  }

  private val tpl = new DBQueryTemplate.Builder[TagType](DB_NAME, "tag", creator).build

  def apply() = tpl

}
