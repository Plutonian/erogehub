package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.HGameApp

object Websites {
  def open(url: String): Unit = HGameApp.app.getHostServices.showDocument(url)
}