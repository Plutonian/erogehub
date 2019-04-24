package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.model.CommonGame
import org.bson.Document

object GuideDB {
  lazy val tlp = new DBOperatorTemplate(DB_NAME, "guide")

  def insert(item: CommonGame.Guide) = {
    val doc = new Document("_id", item.id)
      .append("title", item.title)
      .append("href", item.href)
      .append("from", item.from.getValue)

    tlp.exec(gameC => {
      gameC.insertOne(doc)
    })
  }
}