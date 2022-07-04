package com.goexp.galgame.data

import java.nio.file.Path
import java.util.ResourceBundle

object Config {

  private val prop = ResourceBundle.getBundle("config")
  private val DATA_ROOT = Path.of(prop.getString("DATA_ROOT"))
  val IMG_LOCAL_ROOT = DATA_ROOT.resolve("img/")
}