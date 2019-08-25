package com.goexp.galgame.common

import java.nio.file.Path
import java.util.ResourceBundle

object Config {
  val proxy = true
  //  try //load config
  val prop = ResourceBundle.getBundle("config")
  val DATA_ROOT = Path.of(prop.getString("DATA_ROOT"))
}