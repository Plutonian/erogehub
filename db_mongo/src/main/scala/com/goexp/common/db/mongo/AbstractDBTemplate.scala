package com.goexp.common.db.mongo

import java.util.Objects

import com.mongodb.client.{MongoClient, MongoClients}

object AbstractDBTemplate {
  private val mongoClient: MongoClient = MongoClients.create
}

abstract class AbstractDBTemplate(protected val dbName: String,
                                  protected val tableName: String) {
  protected val mongoClient = AbstractDBTemplate.mongoClient

  Objects.requireNonNull(dbName)
  Objects.requireNonNull(tableName)


}