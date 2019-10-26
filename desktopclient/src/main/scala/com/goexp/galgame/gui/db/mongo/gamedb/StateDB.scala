package com.goexp.galgame.gui.db.mongo.gamedb

import java.util

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{not, eq => equal, _}
import com.mongodb.client.model.Updates.set

object StateDB {
  def update(game: Game): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(equal(game.id), set("state", game.state.get.value))
    })

  def update(brandId: Int): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(
        and(
          equal("brandId", brandId),
          not(equal("state", GameState.SAME.value)),
          not(equal("state", GameState.BLOCK.value))
        )
        , set("state", GameState.BLOCK.value))

    })

  def batchUpdate(games: util.List[Game]): Unit =
    tlp.exec(documentMongoCollection => {
      games.forEach((game: Game) => {
        documentMongoCollection.updateOne(equal(game.id), set("state", game.state.get.value))
      })
    })
}
