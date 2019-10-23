package com.goexp.db.mongo

import org.mongodb.scala.MongoClient

object AbstractDBTemplate {
  private val mongoClient: MongoClient = MongoClient()
}

abstract class AbstractDBTemplate(protected val dbName: String,
                                  protected val tableName: String) {
  protected val mongoClient = AbstractDBTemplate.mongoClient
}