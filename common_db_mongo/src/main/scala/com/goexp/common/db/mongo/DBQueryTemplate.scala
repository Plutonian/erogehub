package com.goexp.common.db.mongo

import java.util._

import org.bson.conversions.Bson

object DBQueryTemplate {

  class Builder[T](dbName: String, tableName: String, creator: ObjectCreator[T]) {
    private var defaultSort: Bson = _
    private var defaultSelect: Bson = _

    def defaultSort(defaultSort: Bson): DBQueryTemplate.Builder[T] = {
      this.defaultSort = defaultSort
      this
    }

    def defaultSelect(defaultSelect: Bson): DBQueryTemplate.Builder[T] = {
      this.defaultSelect = defaultSelect
      this
    }

    def build = new DBQueryTemplate[T](dbName, tableName, creator, defaultSelect, defaultSort)
  }

}

class DBQueryTemplate[T] private(val dbName: String, val tableName: String, val defaultCreator: ObjectCreator[T]) {
  Objects.requireNonNull(defaultCreator)

  private val collection = AbstractDBTemplate.mongoClient.getDatabase(dbName).getCollection(tableName)
  private var defaultSort: Bson = _
  private var defaultSelect: Bson = _

  def this(dbName: String, tableName: String, defaultCreator: ObjectCreator[T], defaultSelect: Bson, defaultSort: Bson) {
    this(dbName, tableName, defaultCreator)
    this.defaultSelect = defaultSelect
    this.defaultSort = defaultSort
  }

  def query = new QueryBuilder

  class QueryBuilder {
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

    def set: Set[T] = getSet(defaultCreator)

    def set(userCreator: ObjectCreator[T]) = {
      Objects.requireNonNull(userCreator)
      getSet(userCreator)
    }

    def list: List[T] = getList(defaultCreator)

    def list(userCreator: ObjectCreator[T]) = {
      Objects.requireNonNull(userCreator)
      getList(userCreator)
    }

    def one: T = {
      var temp = collection.find
      // choice where
      if (where != null) temp = temp.filter(where)
      temp.limit(1).map(defaultCreator.create).first
    }

    def one(userCreator: ObjectCreator[T]): T = {
      Objects.requireNonNull(userCreator)
      var temp = collection.find
      if (where != null) temp = temp.filter(where)
      temp.limit(1).map(userCreator.create).first
    }

    def exists: Boolean = collection.countDocuments(where) > 0

    private def getList(userCreator: ObjectCreator[T]) =
      getDocuments.map(userCreator.create).into(new ArrayList[T])

    private def getSet(userCreator: ObjectCreator[T]) =
      getDocuments.map(userCreator.create).into(new HashSet[T])

    private def getDocuments = {
      var temp = collection.find
      if (where != null) temp = temp.filter(where)
      // choice select
      if (select != null) temp = temp.projection(select)
      else if (defaultSelect != null) temp = temp.projection(defaultSelect)
      // choice sort
      if (sort != null) temp = temp.sort(sort)
      else if (defaultSort != null) temp = temp.sort(defaultSort)
      temp
    }
  }

}