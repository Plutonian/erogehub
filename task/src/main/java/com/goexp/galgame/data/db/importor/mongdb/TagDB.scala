package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.common.db.mysql.DBUpdateTemplate
import com.goexp.galgame.common.model.TagType
import org.bson.Document

import scala.collection.JavaConverters._

object TagDB {
  var tlp = new DBOperatorTemplate("galgame", "tag")
}

class TagDB extends DBUpdateTemplate {

  def insert(item: List[TagType]) = {

    val docs = item
      .toStream
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