package com.goexp.galgame.gui.util.res.gameimg

import com.goexp.galgame.gui.Config
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.ImageCache
import com.typesafe.scalalogging.Logger
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage

import java.net.URL
import java.util.Objects
import javax.imageio.ImageIO
import scala.util.Try

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

      val imgUrl = s"${Config.IMG_REMOTE}/game/${memCacheKey}.jpg"
      logger.debug(s"imgUrl=[${imgUrl}]")

      val triedImage = Try {
        val bufferedImage = ImageIO.read(new URL(imgUrl))
        val writableImage = new WritableImage(bufferedImage.getWidth, bufferedImage.getHeight)
        SwingFXUtils.toFXImage(bufferedImage, writableImage)

        writableImage

      }

      val writableImage = triedImage.getOrElse(null)

      ImageCache().put(memCacheKey, writableImage)
      writableImage

    }

  }
}

