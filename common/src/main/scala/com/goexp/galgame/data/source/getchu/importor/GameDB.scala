package com.goexp.galgame.data.source.getchu.importor

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.jdk.CollectionConverters._

object GameDB {
  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "game")

  def insert(game: Game) = {
    val gameDoc = new Document("_id", game.id)
      .append("isAdult", game.isAdult)
      .append("smallImg", game.smallImg)
      .append("isNew", true)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }

  def updateSmallImg(game: Game): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("smallImg", game.smallImg)
        )
      )
    })


  def updateAll(game: Game) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("name", game.name),
          set("publishDate", game.publishDate),
          set("painter", game.painter),
          set("writer", game.writer),
          set("type", game.`type`),
          set("tag", game.tag),
          set("story", game.story),
          set("intro", game.intro),
          set("state", game.state.value),
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

  def blockAllGame(item: Brand) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(
        and(
          Filters.eq("brandId", item.id),
          Filters.ne("state", GameState.SAME.value),
          Filters.ne("state", GameState.BLOCK.value)
        )
        , set("state", GameState.BLOCK.value))

    })

  def resetState(item: Brand) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(
        Filters.eq("brandId", item.id)
        , set("state", GameState.UNCHECKED.value))

    })

  def exist(id: Int): Boolean = GameFullQuery().where(Filters.eq(id)).exists


  object StateDB {
    def update(game: Game) =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.value))
      })
  }

}

