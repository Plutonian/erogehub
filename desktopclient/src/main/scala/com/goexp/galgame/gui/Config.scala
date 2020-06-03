package com.goexp.galgame.gui

import java.util.ResourceBundle

object Config {
  private val prop = ResourceBundle.getBundle("config")

  val IMG_REMOTE = prop.getString("IMG_REMOTE")
}