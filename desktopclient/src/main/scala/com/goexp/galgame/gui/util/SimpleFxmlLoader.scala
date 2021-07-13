package com.goexp.galgame.gui.util

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Region

import java.net.URL
import scala.reflect.ClassTag

class SimpleFxmlLoader[C](val url: URL) {
  final private val loader = new FXMLLoader(url);
  final val node: Region = loader.load
  final val controller: C = loader.getController

  def this(path: String)(implicit target: ClassTag[C]) = {
    this(target.runtimeClass.getResource(path))
  }

}