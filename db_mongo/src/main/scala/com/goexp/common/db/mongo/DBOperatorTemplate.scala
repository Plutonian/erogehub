package com.goexp.common.db.mongo

import java.util.Objects

import com.mongodb.client.MongoCollection
import org.bson.Document

class DBOperatorTemplate(dbName: String,
                         tableName: String) extends AbstractDBTemplate(dbName, tableName) {
  Objects.requireNonNull(dbName)
  Objects.requireNonNull(tableName)

  def exec(callback: MongoCollection[Document] => Unit): Unit = {
    Objects.requireNonNull(callback)

    val db = mongoClient.getDatabase(dbName)
    callback(db.getCollection(tableName))
  }
}