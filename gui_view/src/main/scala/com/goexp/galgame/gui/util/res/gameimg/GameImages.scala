package com.goexp.galgame.gui.util.res.gameimg

import java.nio.file.Files
import java.util.Objects

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.galgame.common.Config.IMG_PATH
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import com.typesafe.scalalogging.Logger
import javafx.scene.image.Image

object GameImages {
  private val logger = Logger(GameImages.getClass)


  def get(game: Game)(diskCacheKey: String, memCacheKey: String) = {
    Objects.requireNonNull(diskCacheKey)
    Objects.requireNonNull(memCacheKey)

    logger.debug(s"LocalKey=${RED.s(diskCacheKey)},memCacheKey=${memCacheKey}")

    val imageCache = AppCache.imageMemCache
    //try heat cache

    imageCache.get(memCacheKey) match {
      case Some(img) => img
      case None =>
        val localPath = IMG_PATH.resolve(diskCacheKey + ".jpg")
        logger.debug(s"localPath=[${localPath}]")

        //heat disk cache or load from remote
        if (Files.exists(localPath)) {
          val image = new Image("file:" + localPath.toString)
          imageCache.put(memCacheKey, image)
          image
        } else {
          null
        }
    }

  }
}

