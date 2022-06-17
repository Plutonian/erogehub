package com.goexp.galgame.data.source.getchu.importor

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.jdk.CollectionConverters._

object GameDB {
  val tpl = new DBOperator(Config.DB_STRING, DB_NAME, "game")

  def insert(game: Game) = {
    val gameDoc = new Document("_id", game.id)
      //      .append("isAdult", game.isAdult)
      .append("smallImg", game.smallImg)
      .append("isNew", true)

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }

  def updateSmallImg(game: Game): Unit =
    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(
        Filters.eq(game.id),
        combine(
          set("smallImg", game.smallImg)
        )
      )
    })


  def updateAll(game: Game) =
    tpl.exec(documentMongoCollection => {
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
          set("isSame", game.isSame),
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

    tpl.exec(documentMongoCollection => {
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
    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(game.id), set("simpleImg", imgdocs))
    })
  }


  def exist(id: Int): Boolean = GameFullQuery().where(Filters.eq(id)).exists


  object MarkSame {
    def update(game: Game) =
      tpl.exec(documentMongoCollection => {
        documentMongoCollection.updateOne(Filters.eq(game.id), set("isSame", game.isSame))
      })
  }

}

