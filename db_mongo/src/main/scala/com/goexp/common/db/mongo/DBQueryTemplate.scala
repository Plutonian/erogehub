package com.goexp.common.db.mongo

import java.util
import java.util._

import org.bson.conversions.Bson

import scala.jdk.CollectionConverters._

object DBQueryTemplate {

  class Builder[T](private[this] val dbName: String,
                   private[this] val tableName: String,
                   private[this] val creator: ObjectCreator[T]
                  ) {
    private[this] var defaultSort: Bson = _
    private[this] var defaultSelect: Bson = _

    def defaultSort(defaultSort: Bson): Builder[T] = {
      this.defaultSort = defaultSort
      this
    }

    def defaultSelect(defaultSelect: Bson): Builder[T] = {
      this.defaultSelect = defaultSelect
      this
    }

    def build = new DBQueryTemplate[T](dbName, tableName, creator, defaultSelect, defaultSort).query
  }

  def apply[T](dbName: String, tableName: String, defaultCreator: ObjectCreator[T]) = new Builder(dbName, tableName, defaultCreator)

}

class DBQueryTemplate[T] private(
                                  dbName: String,
                                  tableName: String,
                                  private[this] val defaultCreator: ObjectCreator[T]
                                ) extends AbstractDBTemplate(dbName, tableName) {

  Objects.requireNonNull(defaultCreator)

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

  def query = new QueryBuilder


  class QueryBuilder private[DBQueryTemplate] {
    private var where: Bson = _
    private var select: Bson = _
    private var sort: Bson = _

    def where(where: Bson): QueryBuilder = {
      this.where = where
      this
    }

    def select(select: Bson): QueryBuilder = {
      this.select = select
      this
    }

    def sort(sort: Bson): QueryBuilder = {
      this.sort = sort
      this
    }

    private def buildFileIterrableMany = {
      var temp = collection.find()
      if (where != null) temp = temp.filter(where)
      // choice select
      if (select != null) temp = temp.projection(select)
      else if (defaultSelect != null) temp = temp.projection(defaultSelect)
      // choice sort
      if (sort != null) temp = temp.sort(sort)
      else if (defaultSort != null) temp = temp.sort(defaultSort)
      temp
    }

    private def buildFileIterrableOne = {
      var temp = collection.find
      if (where != null) temp = temp.filter(where)
      temp.limit(1)
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


    def one(userCreator: ObjectCreator[T] = defaultCreator): T = {
      Objects.requireNonNull(userCreator)
      buildFileIterrableOne.map(userCreator.create).first
    }

    def exists: Boolean = getDocumentCount > 0


    private def docs2Collection[A <: util.Collection[T]](userCreator: ObjectCreator[T], clazz: A) =
      buildFileIterrableMany.map(userCreator.create).into(clazz)


  }

}