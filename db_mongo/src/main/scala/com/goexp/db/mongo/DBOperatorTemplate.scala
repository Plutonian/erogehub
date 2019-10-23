package com.goexp.db.mongo

import java.util.Objects

import org.mongodb.scala.MongoCollection

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