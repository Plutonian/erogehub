package com.goexp.galgame.common

import com.goexp.galgame.common.db.mongo.query.ConfigQuery
import com.typesafe.scalalogging.Logger

import java.util.ResourceBundle

object Config {
  val proxy = true

  private val prop = ResourceBundle.getBundle("db")

  val DB_STRING = prop.getString("mongo")

  val DB_NAME = prop.getString("database")


  var isInit = false

  protected val logger: Logger = Logger(Config.getClass)

  def initProxy(): Unit =
    if (proxy && !isInit) {

      logger.info("init Network")

      ConfigQuery().one().foreach(c => {

        Option(c.proxy.http).map(_.split(":")).foreach { pear =>
          val host = pear(0)
          val port = pear(1)

          System.setProperty("http.proxyHost", host)
          System.setProperty("http.proxyPort", port)
        }

        Option(c.proxy.https).map(_.split(":")).foreach { pear =>
          val host = pear(0)
          val port = pear(1)

          System.setProperty("https.proxyHost", host)
          System.setProperty("https.proxyPort", port)
        }

        Option(c.proxy.sock5).map(_.split(":")).foreach { pear =>
          val host = pear(0)
          val port = pear(1)

          System.setProperty("socksProxyHost", host)
          System.setProperty("socksProxyPort", port)
        }

      })


      isInit = true
    }
}


case class Configs(proxy: SysProxy)

case class SysProxy(http: String, https: String, sock5: String)