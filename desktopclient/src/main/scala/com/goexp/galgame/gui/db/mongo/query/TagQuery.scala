package com.goexp.galgame.gui.db.mongo.query

import java.util

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.model.TagType
import org.bson.Document
import org.slf4j.LoggerFactory

object TagQuery {
  private val logger = LoggerFactory.getLogger(TagQuery.getClass)
  private val creator: ObjectCreator[TagType] = (doc: Document) => {
    val t = new TagType
    t.`type` = doc.getString("type")
    t.order = doc.getInteger("order")
    t.tags = doc.get("tags", classOf[util.List[String]])
    t
  }
  val tlp = new DBQueryTemplate.Builder[TagType](DB_NAME, "tag", creator).build
}
