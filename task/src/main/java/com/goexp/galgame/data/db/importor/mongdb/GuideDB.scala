package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.common.db.mysql.DBUpdateTemplate
import com.goexp.galgame.common.model.CommonGame
import org.bson.Document

object GuideDB {
  var tlp = new DBOperatorTemplate("galgame", "guide")
}

class GuideDB extends DBUpdateTemplate {
  def insert(item: CommonGame.Guide) = {
    val doc = new Document("_id", item.id)
      .append("title", item.title)
      .append("href", item.href)
      .append("from", item.from.getValue)

    GuideDB.tlp.exec(gameC => {
      gameC.insertOne(doc)
    })
  }
}