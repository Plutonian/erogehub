package com.goexp.galgame.common

import java.util.ResourceBundle

object Config {
  val proxy = true

  private val prop = ResourceBundle.getBundle("db")

  val DB_STRING = prop.getString("mongo")

  val DB_NAME = prop.getString("database")
}