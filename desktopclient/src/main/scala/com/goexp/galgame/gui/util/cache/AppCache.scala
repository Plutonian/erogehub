package com.goexp.galgame.gui.util.cache

import com.goexp.common.cache.SimpleCache
import com.goexp.galgame.gui.model.Brand
import javafx.scene.image.Image

object ImageCache {

  private val imageMemCache = new SimpleCache[String, Image]

  def apply() = imageMemCache

}

object BrandCache {
  private val brandCache = new SimpleCache[Int, Brand]

  def apply() = brandCache
}