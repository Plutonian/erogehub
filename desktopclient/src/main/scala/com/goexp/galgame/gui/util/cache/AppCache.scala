package com.goexp.galgame.gui.util.cache

import com.goexp.galgame.gui.model.Brand
import javafx.scene.image.Image

object ImageCache {

  private val imageMemCache = new Cache[String, Image]

  def apply() = imageMemCache

}

object BrandCache {
  private val brandCache = new Cache[Int, Brand]

  def apply() = brandCache
}