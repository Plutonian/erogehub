package com.goexp.common.db.mongo

import com.mongodb.client.{MongoClient, MongoClients}

object AbstractDBTemplate {
  lazy val mongoClient: MongoClient = MongoClients.create
}