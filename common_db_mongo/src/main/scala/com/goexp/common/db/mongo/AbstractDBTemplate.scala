package com.goexp.common.db.mongo

import com.mongodb.client.{MongoClient, MongoClients}

object AbstractDBTemplate {
  val mongoClient: MongoClient = MongoClients.create
}