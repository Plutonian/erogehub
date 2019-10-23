package com.goexp.db.mongo

import org.mongodb.scala.Document

//import org.bson.Document

@FunctionalInterface trait ObjectCreator[T] {
  def create(doc: Document): T
}