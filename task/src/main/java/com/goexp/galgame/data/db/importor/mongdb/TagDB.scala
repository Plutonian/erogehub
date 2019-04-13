package com.goexp.galgame.data.db.importor.mongdb

import java.util

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.common.db.mysql.DBUpdateTemplate
import com.goexp.galgame.common.model.TagType
import org.bson.Document

import scala.collection.JavaConverters._

object TagDB {
  var tlp = new DBOperatorTemplate("galgame", "tag")
}

class TagDB extends DBUpdateTemplate {

  def insert(item: util.List[TagType]): Unit = {

    val docs = item
      .asScala.toStream
      .map(tagType => {
        new Document("type", tagType.`type`)
          .append("order", tagType.order)
          .append("tags", tagType.tags)
      }).asJava

    TagDB.tlp.exec(gameC => {
      gameC.insertMany(docs)
    })
  }
}