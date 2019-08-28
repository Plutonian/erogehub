package com.goexp.galgame.gui.util.res.gameimg

import java.io.{FileNotFoundException, IOException}
import java.nio.file.{Files, Path}
import java.util.Objects

import com.goexp.galgame.gui.Config.IMG_PATH
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import javax.imageio.ImageIO
import org.slf4j.LoggerFactory

object Util {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def getImage(game: Game)(diskCacheKey: String, memCacheKey: String): Image = {
    Objects.requireNonNull(diskCacheKey)
    Objects.requireNonNull(memCacheKey)

    logger.debug("LocalKey={},memCacheKey={}", diskCacheKey, memCacheKey)
    val imageCache = AppCache.imageMemCache
    //try heat cache

    imageCache.get(memCacheKey) match {
      case Some(img) => img
      case None =>
        val localPath = IMG_PATH.resolve(diskCacheKey + ".jpg")
        logger.debug("localPath={}", localPath)

        //heat disk cache or load from remote
        val image =
        //load from disk
          if (Files.exists(localPath)) {
            new Image("file:" + localPath.toString)
          }
          else {
            def loadFromRemote1(url: String): Image = {
              Objects.requireNonNull(url)
              logger.debug("Remote:{}", url)

              val image = new Image(url, true)
              image.exceptionProperty.addListener((_, _, e: Exception) => {
                if (e != null)
                  if (!e.isInstanceOf[FileNotFoundException])
                    AppCache.imageMemCache.remove(url)
                  else
                    logger.error(e.getMessage)
              })

              image
            }

            def loadFromRemote(url: String)(onLoadOK: (Image) => Unit): Image = {
              val image = loadFromRemote1(url)

              image.progressProperty.addListener((_, _, newValue: Number) => {
                if (newValue != null && newValue.doubleValue == 1)
                  onLoadOK(image)
              })

              image
            }

            //load from remote
            loadFromRemote(memCacheKey) { img =>
              def saveImage(image: Image, path: Path): Unit = {
                Objects.requireNonNull(image)
                Objects.requireNonNull(path)
                val bufferImage = SwingFXUtils.fromFXImage(image, null)
                if (bufferImage == null) return
                try
                  ImageIO.write(bufferImage, "jpg", path.toFile)
                catch {
                  case e: IOException =>
                    println(game)
                    e.printStackTrace()
                }
              }

              if (game.isOkState) {
                Files.createDirectories(localPath.getParent)

                saveImage(img, localPath)
              }
            }
          }

        //memCacheKey as cache key
        imageCache.put(memCacheKey, image)
        image
    }
  }
}