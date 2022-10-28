package com.goexp.galgame.common.util

import com.goexp.galgame.common.Config
import com.typesafe.scalalogging.Logger

object Network {

  protected val logger: Logger = Logger(Network.getClass)

  private var isInit = false

  def initProxy(): Unit = {
    Config.initProxy()


    logger.debug(System.getProperty("http.proxyHost"))
    logger.debug(System.getProperty("http.proxyPort"))

    logger.debug(System.getProperty("https.proxyHost"))
    logger.debug(System.getProperty("https.proxyPort"))

    logger.debug(System.getProperty("socksProxyHost"))
    logger.debug(System.getProperty("socksProxyPort"))
  }
}