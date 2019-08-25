package com.goexp.galgame.gui.db.mongo.gamedb

import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{eq => equal}
import com.mongodb.client.model.Updates.set
import org.slf4j.LoggerFactory

object StarDB {
  private val logger = LoggerFactory.getLogger(StarDB.getClass)

  def update(game: Game): Unit = {
    logger.debug("<update> {}", game)
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(equal(game.id), set("star", game.star))
    })
  }
}
