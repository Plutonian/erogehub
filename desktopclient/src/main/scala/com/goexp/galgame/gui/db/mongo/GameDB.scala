package com.goexp.galgame.gui.db.mongo

import java.util

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set
import org.slf4j.LoggerFactory

object GameDB {
  private val tlp = new DBOperatorTemplate("galgame", "game")

  object StarDB {
    private val logger = LoggerFactory.getLogger(StarDB.getClass)

    def update(game: Game): Unit = {
      logger.debug("<update> {}", game)
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("star", game.star))
      })
    }
  }

  object StateDB {
    def update(game: Game): Unit =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.get.getValue))
      })

    def update(brandId: Int): Unit =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateMany(Filters.eq("brandId", brandId), set("state", GameState.BLOCK.getValue))

      })

    def batchUpdate(games: util.List[Game]): Unit =
      tlp.exec(documentMongoCollection => {
        games.forEach((game: Game) => {
          documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.get.getValue))
        })
      })
  }

}