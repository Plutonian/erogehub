package com.goexp.galgame.gui.db.mongo

import java.util

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.gui.model.Game
import com.mongodb.client.model.Filters.{eq => equal}
import com.mongodb.client.model.Updates.set
import org.slf4j.LoggerFactory
import com.goexp.galgame.common.db.mongo.DB_NAME


object GameDB {
  private val tlp = new DBOperatorTemplate(DB_NAME, "game")

  object StarDB {
    private val logger = LoggerFactory.getLogger(StarDB.getClass)

    def update(game: Game): Unit = {
      logger.debug("<update> {}", game)
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(equal(game.id), set("star", game.star))
      })
    }
  }

  object StateDB {
    def update(game: Game): Unit =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(equal(game.id), set("state", game.state.get.getValue))
      })

    def update(brandId: Int): Unit =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateMany(equal("brandId", brandId), set("state", GameState.BLOCK.getValue))

      })

    def batchUpdate(games: util.List[Game]): Unit =
      tlp.exec(documentMongoCollection => {
        games.forEach((game: Game) => {
          documentMongoCollection.updateOne(equal(game.id), set("state", game.state.get.getValue))
        })
      })
  }

}