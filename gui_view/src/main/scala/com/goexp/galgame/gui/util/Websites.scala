package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.HGameApp.app

object Websites {
  def open(url: String): Unit = app.getHostServices.showDocument(url)
}