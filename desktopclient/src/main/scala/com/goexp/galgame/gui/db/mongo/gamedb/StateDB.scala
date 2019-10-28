package com.goexp.galgame.gui.db.mongo.gamedb

import java.util

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set

object StateDB {
  def update(game: Game): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.get.value))
    })

  def blockAllGame(brandId: Int): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(
        Filters.and(
          Filters.eq("brandId", brandId),
          Filters.ne("state", GameState.SAME.value),
          Filters.ne("state", GameState.BLOCK.value)
        )
        , set("state", GameState.BLOCK.value))

    })

  def batchUpdate(games: util.List[Game]): Unit =
    tlp.exec(documentMongoCollection => {
      games.forEach(game => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.get.value))
      })
    })
}
