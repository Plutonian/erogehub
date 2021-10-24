package com.goexp.galgame.gui.util.cache

import com.goexp.common.cache.SimpleCache
import javafx.scene.image.Image

object ImageCache {

  private val imageMemCache = new SimpleCache[String, Image]

  def apply() = imageMemCache

}