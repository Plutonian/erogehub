package com.goexp.galgame.data.source.getchu.importor

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.goexp.galgame.data.source.getchu.query.GameQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.jdk.CollectionConverters._

object GameDB {
  val tlp = new DBOperatorTemplate(DB_NAME, "game")

  def insert(game: Game) = {
    val gameDoc = new Document("_id", game.id)
      .append("name", game.name)
      .append("publishDate", game.publishDate)
      .append("smallImg", game.smallImg)
      .append("state", 0)
      .append("star", 0)
      .append("state", game.state.value)
      .append("brandId", game.brandId)
      .append("isNew", game.isNew)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }

  def update(game: Game): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("publishDate", game.publishDate),
          set("smallImg", game.smallImg)
        )
      )
    })

  def update(id: Int, middle: Int, website: String, group: String): Unit =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(id),
        combine(
          set("middle", middle),
          set("website", website),
          set("group", group)
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

  def exist(id: Int): Boolean = GameQuery.fullTlp.where(Filters.eq(id)).exists


  object StateDB {
    def update(game: Game) =
      tlp.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("state", game.state.value))
      })
  }

}

