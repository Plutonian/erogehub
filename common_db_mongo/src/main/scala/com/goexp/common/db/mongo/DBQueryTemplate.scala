package com.goexp.common.db.mongo

import java.util
import java.util._

import org.bson.conversions.Bson

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

    def build = new DBQueryTemplate[T](dbName, tableName, creator, defaultSelect, defaultSort)
  }

}

class DBQueryTemplate[T] private(dbName: String,
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
    private[this] val part = new PartBuilder
    private[this] val finalPart = new FinalBuilder

    def where(where: Bson): QueryBuilder = {
      part.where(where)
      this
    }

    def select(select: Bson): QueryBuilder = {
      part.select(select)
      this
    }

    def sort(sort: Bson): QueryBuilder = {
      part.sort(sort)
      this
    }

    def set: util.Set[T] = finalPart.set

    def set(userCreator: ObjectCreator[T]) = {
      Objects.requireNonNull(userCreator)
      finalPart.set(userCreator)
    }

    def list: util.List[T] = finalPart.list

    def list(userCreator: ObjectCreator[T]) = {
      Objects.requireNonNull(userCreator)
      finalPart.list(userCreator)
    }

    def one: T = finalPart.one

    def one(userCreator: ObjectCreator[T]): T = {
      Objects.requireNonNull(userCreator)
      finalPart.one(userCreator)
    }

    def exists: Boolean = finalPart.exists


    class PartBuilder private[QueryBuilder] {
      private var where: Bson = _
      private var select: Bson = _
      private var sort: Bson = _

      def where(where: Bson): PartBuilder = {
        this.where = where
        this
      }

      def select(select: Bson): PartBuilder = {
        this.select = select
        this
      }

      def sort(sort: Bson): PartBuilder = {
        this.sort = sort
        this
      }

      def buildFileIterrableMany = {
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

      def buildFileIterrableOne = {
        var temp = collection.find
        if (where != null) temp = temp.filter(where)
        temp.limit(1)
      }

      def getDocumentCount = {
        collection.countDocuments(where)
      }
    }

    class FinalBuilder private[QueryBuilder] {
      def set: util.Set[T] = docs2Collection(defaultCreator.create, new util.HashSet[T])

      def set(userCreator: ObjectCreator[T]) = {
        Objects.requireNonNull(userCreator)
        docs2Collection(userCreator.create, new util.HashSet[T])
      }

      def list: util.List[T] = docs2Collection(defaultCreator.create, new util.ArrayList[T])

      def list(userCreator: ObjectCreator[T]) = {
        Objects.requireNonNull(userCreator)
        docs2Collection(userCreator.create, new util.ArrayList[T])
      }

      def one: T = {
        part.buildFileIterrableOne.map(defaultCreator.create).first
      }

      def one(userCreator: ObjectCreator[T]): T = {
        Objects.requireNonNull(userCreator)
        part.buildFileIterrableOne.map(userCreator.create).first
      }

      def exists: Boolean = part.getDocumentCount > 0


      private def docs2Collection[A <: util.Collection[T]](userCreator: ObjectCreator[T], clazz: A) =
        part.buildFileIterrableMany.map(userCreator.create).into(clazz)

    }

  }

}