package com.goexp.galgame.common

import java.nio.file.Path
import java.util.ResourceBundle

object Config {
  val proxy = true

  private val prop = ResourceBundle.getBundle("config")
  private val DATA_ROOT = Path.of(prop.getString("DATA_ROOT"))
  val IMG_PATH = DATA_ROOT.resolve("img/game/")
}