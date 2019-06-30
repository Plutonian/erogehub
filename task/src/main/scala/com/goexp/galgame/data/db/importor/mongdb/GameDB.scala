package com.goexp.galgame.data.db.importor.mongdb

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.jdk.CollectionConverters._

object GameDB {
  lazy val tlp = new DBOperatorTemplate(DB_NAME, "game")

  def insert(game: Game) = {
    val gameDoc = new Document("_id", game.id)
      .append("name", game.name)
      .append("publishDate", game.publishDate)
      .append("smallImg", game.smallImg)
      .append("state", 0)
      .append("star", 0)
      .append("state", game.state.value)
      .append("brandId", game.brandId)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }

  def update(game: Game) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("publishDate", game.publishDate),
          set("smallImg", game.smallImg)
        )
      )
    })

  def updateAll(game: Game) =
    tlp.exec(documentMongoCollection => {
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

  def updateChar(game: Game) = {
    val gameCharDocs = game.gameCharacters
      .asScala.to(LazyList)
      .map(person => {
        new Document("name", person.name)
          .append("intro", person.intro)
          .append("cv", person.cv)
          .append("truecv", person.trueCV)
          .append("img", person.img)
          .append("index", person.index)
      }).asJava

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("gamechar", gameCharDocs))
    })
  }

  def updateImg(game: Game) = {
    val imgdocs = game.gameImgs
      .asScala.to(LazyList)
      .map(img => {
        new Document("src", img.src)
          .append("index", img.index)
      }).asJava
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("simpleImg", imgdocs))
    })
  }

  def exist(id: Int): Boolean = GameQuery.fullTlp.query.where(Filters.eq(id)).exists


  object StateDB {
    def update(game: Game) =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.value))
      })
  }

}

