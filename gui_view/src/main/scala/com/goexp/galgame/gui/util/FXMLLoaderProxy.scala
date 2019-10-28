package com.goexp.galgame.gui.util

import java.net.URL

import javafx.fxml.FXMLLoader

class FXMLLoaderProxy[N, C](val url: URL) {
  final private val loader = new FXMLLoader(url);
  final val node: N = loader.load
  final val controller: C = loader.getController

  def this(path: String) {
    this(classOf[FXMLLoaderProxy[N, C]].getClassLoader.getResource(path))
  }

}