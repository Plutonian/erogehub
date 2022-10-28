package com.goexp.galgame.common.db.mongo.query

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.{Config, Configs, SysProxy}
import com.typesafe.scalalogging.Logger
import org.bson.Document

object ConfigQuery {

  object ConfigCreator extends ObjectCreator[Configs] {

    private val logger = Logger(ConfigCreator.getClass)


    override def create(doc: Document): Configs = {


      logger.trace(s"<Doc> $doc")


      val proxydoc = doc.get("proxy").asInstanceOf[Document]

      val http = proxydoc.getString("http")
      val https = proxydoc.getString("https")
      val sock5 = proxydoc.getString("sock5")
      Configs(SysProxy(http, https, sock5))
    }
  }


  private val tlp = DBQuery[Configs](Config.DB_STRING, DB_NAME, "sys", ConfigCreator).build

  def apply() = tlp

  def main(args: Array[String]): Unit = {
    ConfigQuery().one().foreach(println)
  }

}
