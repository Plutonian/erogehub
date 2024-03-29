package com.goexp.galgame.data.source.getchu.importor

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.TagType
import org.bson.Document

import scala.jdk.CollectionConverters._

object TagDB {

  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "tag")

  def insert(item: List[TagType]) = {
    val docs = item
      .to(LazyList)
      .map(tagType => {
        new Document("type", tagType.name)
          .append("order", tagType.order)
          .append("tags", tagType.tags)
      }).asJava

    tlp.exec(gameC => {
      gameC.insertMany(docs)
    })
  }
}