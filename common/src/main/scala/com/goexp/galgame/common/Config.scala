package com.goexp.galgame.common

import java.util.ResourceBundle

object Config {
  val proxy = true

  private val prop = ResourceBundle.getBundle("config_common")

  val DB_STRING = prop.getString("mongo")

  val DB_NAME = "galgame"
}