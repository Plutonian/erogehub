package com.goexp.galgame.common.db.mongo.query

import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.mongodb.client.{MongoClient, MongoClients, MongoCollection}
import org.bson.Document

object GameQuery {

  lazy val query: MongoCollection[Document] = mongoClient.getDatabase(DB_NAME).getCollection("game")
  private lazy val mongoClient: MongoClient = MongoClients.create(Config.DB_STRING)

  def apply() = query

}