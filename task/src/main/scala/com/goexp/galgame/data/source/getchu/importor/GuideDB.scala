package com.goexp.galgame.data.source.getchu.importor

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.model.game.guide.GameGuide
import com.mongodb.client.model.{Filters, Updates}
import org.bson.Document

object GuideDB {
  val tlp = new DBOperatorTemplate(DB_NAME, "guide")

  def insert(item: GameGuide) = {
    val doc = new Document("_id", item.id)
      .append("title", item.title)
      .append("href", item.href)
      .append("from", item.from.value)

    tlp.exec(gameC => {
      gameC.insertOne(doc)
    })
  }

  def update(item: GameGuide) = {
    tlp.exec(gameC => {
      gameC.updateOne(
        Filters.eq("_id", item.id), Updates.set("html", item.html))
    })
  }
}