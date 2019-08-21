package com.goexp.galgame.common.util

import java.util.ResourceBundle

import com.goexp.galgame.common.Config

object Network {
  private var isInit = false

  def initProxy(): Unit =
    if (Config.proxy && !isInit) {
      val prop = ResourceBundle.getBundle("proxy")

      prop.keySet.forEach((key: String) => {
        System.setProperty(key, prop.getString(key))
      })
      isInit = true
    }
}