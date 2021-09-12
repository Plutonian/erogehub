package com.goexp.galgame.gui.util

trait DataSource {

  def load(): Unit

  def dispose(): Unit

}
