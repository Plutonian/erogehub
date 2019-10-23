package com.goexp.common.db.mongo

import com.mongodb.client.{MongoClient, MongoClients}

object AbstractDBTemplate {
  private lazy val mongoClient: MongoClient = MongoClients.create
}

abstract class AbstractDBTemplate(protected val dbName: String,
                                  protected val tableName: String) {
  protected val mongoClient = AbstractDBTemplate.mongoClient
}