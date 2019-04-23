package com.goexp.common.db.mongo

import java.util.Objects

import com.mongodb.client.MongoCollection
import org.bson.Document

class DBOperatorTemplate(private[this] val dbName: String,
                         private[this] val tableName: String) {
  Objects.requireNonNull(dbName)
  Objects.requireNonNull(tableName)

  def exec(callback: MongoCollection[Document] => Unit): Unit = {
    Objects.requireNonNull(callback)

    val db = AbstractDBTemplate.mongoClient.getDatabase(dbName)
    callback(db.getCollection(tableName))
  }
}