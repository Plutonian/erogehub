package com.goexp.common.db.mongo

import java.util.Objects

import com.mongodb.client.{MongoClient, MongoClients}

object DBTemplate {
  private val mongoClient: MongoClient = MongoClients.create
}

abstract class DBTemplate(protected val database: String,
                          protected val table: String) {
  protected val mongoClient = DBTemplate.mongoClient

  Objects.requireNonNull(database)
  Objects.requireNonNull(table)


}