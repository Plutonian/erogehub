package com.goexp.galgame.data.source.erogamescape.importor

import java.time.LocalDate

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.data.source.erogamescape.DB_NAME
import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser.{OutLink, Tags}
import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser.PageItem
import com.goexp.galgame.data.source.erogamescape.query.GameQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}
import org.bson.Document

import scala.jdk.CollectionConverters._

object GameDB {
  def exist(id: Int): Boolean = {
    GameQuery.simpleTlp.where(Filters.eq(id)).exists
  }

  val tlp = new DBOperatorTemplate(DB_NAME, "game")

  def insert(pageItem: PageItem) = {
    val gameDoc = new Document("_id", pageItem.id)
      .append("name", pageItem.name)
      .append("brandId", pageItem.brandId)
      .append("middle", pageItem.middle)
      .append("pian", pageItem.pian)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.insertOne(gameDoc)
    })
  }


  //  def updateAll(game: Game) =
  //    tlp.exec(documentMongoCollection => {
  //      documentMongoCollection.updateOne(
  //        Filters.eq(game.id),
  //        combine(
  //          set("painter", game.painter),
  //          set("writer", game.writer),
  //          set("type", game.`type`),
  //          set("tag", game.tag),
  //          set("story", game.story),
  //          set("intro", game.intro),
  //          set("brandId", game.brandId))
  //      )
  //    })

  def updateContent(id: Int, outLink: OutLink, tags: LazyList[Tags], group: String, date: LocalDate) = {
    tlp.exec(documentMongoCollection => {

      val tagesS = tags.map { case Tags(k, v) =>
        new Document("k", k)
          .append("v", v)
      }.asJava


      documentMongoCollection.updateOne(
        Filters.eq(id),
        combine(
          set("publishDate", date),
          set("getchuId", outLink.getchuId),
          set("website", outLink.gHP),
          set("dmmId", outLink.DMMID),
          set("group", group),
          set("tags", tagesS)
        )
      )
    })
  }

  //  def updateChar(game: Game) = {
  //    val gameCharDocs = game.gameCharacters
  //      .asScala.to(LazyList)
  //      .map(person => {
  //        new Document("name", person.name)
  //          .append("intro", person.intro)
  //          .append("cv", person.cv)
  //          .append("truecv", person.trueCV)
  //          .append("img", person.img)
  //          .append("index", person.index)
  //      }).asJava
  //
  //    tlp.exec(documentMongoCollection => {
  //      documentMongoCollection.updateOne(Filters.eq(game.id), set("gamechar", gameCharDocs))
  //    })
  //  }
  //
  //  def updateImg(game: Game) = {
  //    val imgdocs = game.gameImgs
  //      .asScala.to(LazyList)
  //      .map(img => {
  //        new Document("src", img.src)
  //          .append("index", img.index)
  //      }).asJava
  //    tlp.exec(documentMongoCollection => {
  //      documentMongoCollection.updateOne(Filters.eq(game.id), set("simpleImg", imgdocs))
  //    })
  //  }

}

