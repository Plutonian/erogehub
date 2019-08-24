package com.goexp.galgame.gui.util.cache

import com.goexp.galgame.gui.model.Brand
import java.util

object AppCache {
  val brandCache = new util.HashMap[Integer, Brand]
  val imageMemCache = new ImageMemCache
}