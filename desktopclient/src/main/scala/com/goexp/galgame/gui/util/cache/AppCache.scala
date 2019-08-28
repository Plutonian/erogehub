package com.goexp.galgame.gui.util.cache

import com.goexp.galgame.gui.model.Brand
import java.util

import javafx.scene.image.Image

object AppCache {
  val brandCache = new util.HashMap[Integer, Brand]
  val imageMemCache = new Cache[String, Image]
}