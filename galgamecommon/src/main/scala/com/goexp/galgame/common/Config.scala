package com.goexp.galgame.common

import java.nio.file.Path
import java.util.ResourceBundle

object Config {
//  var DATA_ROOT: Path = null
  val proxy = true
//  try //load config
  val prop: ResourceBundle = ResourceBundle.getBundle("config")
  val DATA_ROOT = Path.of(prop.getString("DATA_ROOT"))
}