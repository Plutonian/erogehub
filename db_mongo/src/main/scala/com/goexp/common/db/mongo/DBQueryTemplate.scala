package com.goexp.common.db.mongo

import java.util
import java.util.Objects

import org.bson.conversions.Bson

import scala.jdk.CollectionConverters._

object DBQueryTemplate {

  class Builder[T] private[DBQueryTemplate](private val dbName: String,
                                            private val tableName: String,
                                            private val creator: ObjectCreator[T]
                                           ) {
    private var defaultSort: Bson = _
    private var defaultSelect: Bson = _

    def defaultSort(defaultSort: Bson): Builder[T] = {
      this.defaultSort = defaultSort
      this
    }

    def defaultSelect(defaultSelect: Bson): Builder[T] = {
      this.defaultSelect = defaultSelect
      this
    }

    def build = new DBQueryTemplate[T](dbName, tableName, creator, defaultSelect, defaultSort)
  }

  def apply[T](dbName: String, tableName: String, defaultCreator: ObjectCreator[T]) = new Builder(dbName, tableName, defaultCreator)

}

class DBQueryTemplate[T] private(dbName: String,
                                 tableName: String,
                                 private val defaultCreator: ObjectCreator[T]
                                ) extends DBTemplate(dbName, tableName) {

  //init start

  require(defaultCreator != null, "defaultCreator can't be null")

  //init end

  private val collection = mongoClient.getDatabase(dbName).getCollection(tableName)
  private var defaultSort: Bson = _
  private var defaultSelect: Bson = _

  def this(dbName: String,
           tableName: String,
           defaultCreator: ObjectCreator[T],
           defaultSelect: Bson,
           defaultSort: Bson) {
    this(dbName, tableName, defaultCreator)
    this.defaultSelect = defaultSelect
    this.defaultSort = defaultSort
  }

  private var where: Bson = _
  private var select: Bson = _
  private var sort: Bson = _

  def where(where: Bson): DBQueryTemplate[T] = {
    this.where = where
    this
  }

  def select(select: Bson): DBQueryTemplate[T] = {
    this.select = select
    this
  }

  def sort(sort: Bson): DBQueryTemplate[T] = {
    this.sort = sort
    this
  }

  private def buildFileIterrableMany = {
    collection.find()
      .filter(where)
      .projection(Option(defaultSelect).getOrElse(select))
      .sort(Option(defaultSort).getOrElse(sort))
  }

  private def buildFileIterrableOne = {
    collection.find()
      .filter(where)
      .limit(1)
  }

  private def getDocumentCount = {
    collection.countDocuments(where)
  }

  def set(userCreator: ObjectCreator[T] = defaultCreator) = {
    Objects.requireNonNull(userCreator)
    docs2Collection(userCreator.create, new util.HashSet[T])
  }


  def list(userCreator: ObjectCreator[T] = defaultCreator) = {
    Objects.requireNonNull(userCreator)
    docs2Collection(userCreator.create, new util.ArrayList[T])
  }


  def scalaList(userCreator: ObjectCreator[T] = defaultCreator) = list(userCreator).asScala


  def one(userCreator: ObjectCreator[T] = defaultCreator): Option[T] = {
    Objects.requireNonNull(userCreator)

    val iterable = buildFileIterrableOne.map(userCreator.create)

    Option(iterable.first)
  }


  def exists: Boolean = getDocumentCount > 0


  private def docs2Collection[A <: util.Collection[T]](userCreator: ObjectCreator[T], clazz: A) =
    buildFileIterrableMany.map(userCreator.create).into(clazz)

}