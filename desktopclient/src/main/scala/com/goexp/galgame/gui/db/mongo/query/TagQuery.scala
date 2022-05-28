package com.goexp.galgame.gui.db.mongo.query

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.TagType
import com.typesafe.scalalogging.Logger
import org.bson.Document

import java.util
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

  private val tpl = DBQuery[TagType](Config.DB_STRING, DB_NAME, "tag", creator).build

  def apply() = tpl

}
