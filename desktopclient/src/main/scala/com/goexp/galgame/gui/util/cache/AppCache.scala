package com.goexp.galgame.gui.util.cache

import com.goexp.common.cache.SimpleCache
import com.goexp.galgame.gui.db.mongo.query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import javafx.scene.image.Image

object ImageCache {

  private val imageMemCache = new SimpleCache[String, Image]

  def apply() = imageMemCache

}