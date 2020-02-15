package com.goexp.galgame.gui.db.mongo.gamedb

import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{eq => equal}
import com.mongodb.client.model.Updates.set
import com.typesafe.scalalogging.Logger

object StarDB {
  private val logger = Logger(StarDB.getClass)

  def update(game: Game): Unit = {

    logger.debug(s"<update> ${game}")

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(equal(game.id), set("star", game.star.get()))
    })
  }
}
