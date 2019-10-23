package com.goexp.db.mongo

import java.util.Objects

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.{Document, Observer}

import scala.collection.mutable

object QueryBuilder {
  def apply[T](dbName: String, tableName: String, creator: ObjectCreator[T]) = new QueryBuilder(dbName, tableName, creator)
}

class QueryBuilder[T] private(private[this] val dbName: String,
                              private[this] val tableName: String,
                              private[this] val creator: ObjectCreator[T]
                             ) {
  private[this] var defaultSort: Bson = _
  private[this] var defaultSelect: Bson = _

  def defaultSort(defaultSort: Bson): QueryBuilder[T] = {
    this.defaultSort = defaultSort
    this
  }

  def defaultSelect(defaultSelect: Bson): QueryBuilder[T] = {
    this.defaultSelect = defaultSelect
    this
  }

  def build = new DBQueryTemplate[T](dbName, tableName, creator, defaultSelect, defaultSort)

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
      temp.first()
    }

    private def getDocumentCount()(onOK: Long => Unit) = {
      collection.countDocuments(where)
        .subscribe(new Observer[Long] {
          var docCount: Long = _

          override def onNext(result: Long): Unit = {
            this.docCount = result
          }

          override def onError(e: Throwable): Unit = e.printStackTrace()

          override def onComplete(): Unit = onOK(docCount)
        })
    }


    def set(userCreator: ObjectCreator[T])(onOK: Set[T] => Unit) = {
      Objects.requireNonNull(userCreator)
      buildFileIterrableMany
        .subscribe(new Observer[Document] {
          val sets = mutable.Set[T]()

          override def onNext(result: Document): Unit = {
            sets.addOne(userCreator.create(result))
          }

          override def onError(e: Throwable): Unit = {
            e.printStackTrace()
          }

          override def onComplete(): Unit = {
            onOK(sets.toSet)
          }
        })
    }

    def list(userCreator: ObjectCreator[T]): (List[T] => Unit) => Unit = {
      Objects.requireNonNull(userCreator)
      docs2Collection(userCreator.create)
    }

    def one(userCreator: ObjectCreator[T])(onOK: T => Unit) = {
      Objects.requireNonNull(userCreator)

      buildFileIterrableOne.subscribe(new Observer[Document] {
        private var target: Any = _

        override def onNext(result: Document): Unit = {
          target = userCreator.create(result)
        }

        override def onError(e: Throwable): Unit = {
          e.printStackTrace()
        }

        override def onComplete(): Unit = {
          onOK(target.asInstanceOf[T])
        }
      })
    }

    def exists: (Long => Unit) => Unit = getDocumentCount()


    private def docs2Collection(userCreator: ObjectCreator[T])(onOK: List[T] => Unit) = {
      buildFileIterrableMany.subscribe(new Observer[Document] {
        val list = mutable.ListBuffer[T]()

        override def onNext(result: Document): Unit = {
          list.addOne(userCreator.create(result))
        }

        override def onError(e: Throwable): Unit = {
          e.printStackTrace()
        }

        override def onComplete(): Unit = {
          onOK(list.toList)
        }
      })

    }
  }

}