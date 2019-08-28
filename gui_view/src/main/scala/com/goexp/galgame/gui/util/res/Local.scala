package com.goexp.galgame.gui.util.res

import com.goexp.galgame.gui.util.cache.{AppCache, Cache}
import javafx.scene.image.Image

object Local {
  def getLocal(name: String): Image = getLocal(name, AppCache.imageMemCache)

  private def getLocal(name: String, imageMemCache: Cache[String, Image]) =
    imageMemCache.get(name) match {
      case Some(img) => img
      case None =>
        val image = new Image(this.getClass.getResource(name).toExternalForm)
        imageMemCache.put(name, image)
        image
    }
}