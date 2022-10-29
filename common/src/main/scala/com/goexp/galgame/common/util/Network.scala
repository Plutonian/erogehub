package com.goexp.galgame.common.util

import com.goexp.galgame.common.Config
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object Network {

  protected val logger: Logger = Logger(Network.getClass)

  def initProxy(): Unit = {
    Config.initProxy()


    System.getProperties.asScala
      .to(LazyList)
      .filter { case (k, _) => k.contains("proxy") }
      .foreach { case (k, v) =>

        logger.info(s"$k:$v")
      }
  }

  def main(args: Array[String]): Unit = {
    initProxy()
  }
}