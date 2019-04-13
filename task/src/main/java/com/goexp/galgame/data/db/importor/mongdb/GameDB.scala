package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.collection.JavaConverters._


object GameDB {
  var tlp = new DBOperatorTemplate("galgame", "game")

  class StateDB {
    def update(game: Game): Unit =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.getValue))
      })
  }

}

class GameDB {
  def insert(game: Game): Unit = {
    val gameDoc = new Document("_id", game.id)
      .append("name", game.name)
      .append("publishDate", game.publishDate)
      .append("smallImg", game.smallImg)
      .append("state", 0)
      .append("star", 0)
      .append("state", game.state.getValue)
      .append("brandId", game.brandId)

    GameDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }

  def update(game: Game): Unit =
    GameDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("publishDate", game.publishDate),
          set("smallImg", game.smallImg)
        )
      )
    })

  def updateAll(game: Game): Unit =
    GameDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("painter", game.painter),
          set("writer", game.writer),
          set("type", game.`type`),
          set("tag", game.tag),
          set("story", game.story),
          set("intro", game.intro),
          set("brandId", game.brandId))
      )
    })

  def updateChar(game: Game): Unit = {
    val gameCharDocs = game.gameCharacters
      .asScala.toStream
      .map(person => {
        new Document("name", person.name)
          .append("intro", person.intro)
          .append("cv", person.cv)
          .append("truecv", person.trueCV)
          .append("img", person.img)
          .append("index", person.index)
      }).asJava

    GameDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("gamechar", gameCharDocs))
    })
  }

  def updateImg(game: Game): Unit = {
    val imgdocs = game.gameImgs
      .asScala.toStream
      .map(img => {
        new Document("src", img.src)
          .append("index", img.index)
      }).asJava
    GameDB.tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("simpleImg", imgdocs))
    })
  }

  def exist(id: Int): Boolean = GameQuery.fullTlp.query.where(Filters.eq(id)).exists
}