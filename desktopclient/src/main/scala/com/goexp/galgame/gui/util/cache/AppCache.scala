package com.goexp.galgame.gui.util.cache

import com.goexp.galgame.gui.model.Brand
import javafx.scene.image.Image

object AppCache {

  val imageMemCache = new Cache[String, Image]

}

object BrandCache {
  val brandCache = new Cache[Int, Brand]

  def apply() = brandCache
}