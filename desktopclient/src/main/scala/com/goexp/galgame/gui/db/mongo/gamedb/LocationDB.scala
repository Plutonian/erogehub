package com.goexp.galgame.gui.db.mongo.gamedb

import java.util

import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set

object LocationDB {

  def batchUpdate(games: util.List[Game]): Unit =
    tlp.exec(documentMongoCollection => {
      games.forEach(game => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("location", game.location.get.value))
      })
    })
}
