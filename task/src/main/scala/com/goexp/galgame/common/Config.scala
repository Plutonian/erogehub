package com.goexp.galgame.common

import com.goexp.common.util.string.Strings
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

        Option(c.proxy.http)
          .filter(s => Strings.isNotEmpty(s))
          .map(_.split(":"))
          .foreach { pear =>
            val host = pear(0)
            val port = pear(1)

            logger.debug(s"[PROXY] http://$host:$port")

            System.setProperty("http.proxyHost", host)
            System.setProperty("http.proxyPort", port)
          }

                // https 代理：若 DB 未配 proxy.https，回退使用 http 代理（多数 http 代理同样支持 CONNECT 隧道）
        // 否则 https.proxyHost 为空 → HttpClient 直连 getchu:443 → 被防火墙 RST → SSLHandshakeException
        val httpsProxy = Option(c.proxy.https).filter(Strings.isNotEmpty).getOrElse(c.proxy.http)
        Option(httpsProxy)
          .filter(s => Strings.isNotEmpty(s))
          .map(_.split(":")).foreach { pear =>
          val host = pear(0)
          val port = pear(1)

            logger.debug(s"[PROXY] https://$host:$port")


            System.setProperty("https.proxyHost", host)
          System.setProperty("https.proxyPort", port)
        }

        Option(c.proxy.sock5)
          .filter(s => Strings.isNotEmpty(s))
          .map(_.split(":"))
          .foreach { pear =>
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