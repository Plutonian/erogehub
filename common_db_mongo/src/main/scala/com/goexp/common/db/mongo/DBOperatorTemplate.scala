package com.goexp.common.db.mongo

import java.util.Objects
import java.util.function.Consumer

import com.mongodb.client.MongoCollection
import org.bson.Document

class DBOperatorTemplate(val dbName: String, val tableName: String) {
  Objects.requireNonNull(dbName)
  Objects.requireNonNull(tableName)

  def exec(back: Consumer[MongoCollection[Document]]): Unit = {
    Objects.requireNonNull(back)


    val db = AbstractDBTemplate.mongoClient.getDatabase(dbName)
    back.accept(db.getCollection(tableName))
  }
}