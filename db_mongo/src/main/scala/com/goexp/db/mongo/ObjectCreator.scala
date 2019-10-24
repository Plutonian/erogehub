package com.goexp.db.mongo

import org.mongodb.scala.Document

@FunctionalInterface trait ObjectCreator[T] {
  def create(doc: Document): T
}