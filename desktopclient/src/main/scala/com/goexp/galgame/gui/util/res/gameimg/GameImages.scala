package com.goexp.galgame.gui.util.res.gameimg

import java.util.Objects

import com.goexp.galgame.gui.Config
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.ImageCache
import com.typesafe.scalalogging.Logger
import javafx.scene.image.Image

object GameImages {
  private val logger = Logger(GameImages.getClass)


  def get(game: Game)(diskCacheKey: String, memCacheKey: String) = {
    getFromUrl(game)(memCacheKey)
  }

  def getFromUrl(game: Game)(memCacheKey: String) = {
    Objects.requireNonNull(memCacheKey)

    logger.debug(s"memCacheKey=${memCacheKey}")

    //try heat cache

    ImageCache().get(memCacheKey).getOrElse {

      val imgUrl = s"${Config.IMG_REMOTE}/${memCacheKey}.jpg"
      logger.debug(s"imgUrl=[${imgUrl}]")

      val image = new Image(imgUrl, true)
      ImageCache().put(memCacheKey, image)
      image
    }

  }
}

