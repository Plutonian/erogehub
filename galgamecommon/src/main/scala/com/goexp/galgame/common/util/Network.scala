package com.goexp.galgame.common.util

import java.util.ResourceBundle

import com.goexp.galgame.common.Config
import com.typesafe.scalalogging.Logger

object Network {

  protected val logger: Logger = Logger(Network.getClass)

  private var isInit = false

  def initProxy(): Unit =
    if (Config.proxy && !isInit) {

      logger.info("init Network")

      val prop = ResourceBundle.getBundle("proxy")


      prop.keySet.forEach(k => {
        val v = prop.getString(k)

        logger.debug("{}->{}",k,v)

        System.setProperty(k, v)
      })


      isInit = true
    }
}