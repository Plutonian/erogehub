package com.goexp.galgame.gui.util.res.gameimg

import java.io.{ByteArrayOutputStream, FileNotFoundException, IOException}
import java.nio.file.{Files, Path}
import java.util.Objects

import com.goexp.galgame.gui.Config
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import javax.imageio.ImageIO
import org.slf4j.LoggerFactory

object Util {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def getImage(game: Game, cacheKey: (String, String)): Image = {

    val (diskCacheKey, memCacheKey) = cacheKey

    Objects.requireNonNull(cacheKey)
    Objects.requireNonNull(diskCacheKey)
    Objects.requireNonNull(memCacheKey)

    logger.debug("LocalKey={},memCacheKey={}", diskCacheKey, memCacheKey)
    val imageCache = AppCache.imageMemCache
    //try heat cache

    imageCache.get(memCacheKey) match {
      case Some(img) => img
      case None =>
        val localPath = Config.IMG_PATH.resolve(diskCacheKey + ".jpg")
        logger.debug("localPath={}", localPath)

        //heat disk cache or load from remote
        val image =
          if (Files.exists(localPath))
            fromDisk(localPath)
          else
            loadRemote(memCacheKey) { img =>
              if (game.isOkState) {
                try Files.createDirectories(localPath.getParent)
                catch {
                  case e: IOException =>
                    e.printStackTrace()
                }
                saveImage(img, localPath)
              }
            }

        //memCacheKey as cache key
        imageCache.put(memCacheKey, image)
        image
    }
  }


  def preLoadRemoteImage(cacheKey: (String, String)): Unit = {

    val (diskCacheKey, memCacheKey) = cacheKey

    Objects.requireNonNull(cacheKey)
    Objects.requireNonNull(diskCacheKey)
    Objects.requireNonNull(memCacheKey)
    logger.debug("LocalKey={},memCacheKey={}", diskCacheKey, memCacheKey)
    val imageCache = AppCache.imageMemCache
    val cachedImage = imageCache.get(memCacheKey)
    if (cachedImage.isEmpty) {
      val localPath = Config.IMG_PATH.resolve(diskCacheKey + ".jpg")
      logger.debug("localPath={}", localPath)
      if (!Files.exists(localPath))
        loadRemote(memCacheKey) { (image1: Image) => {
          Files.createDirectories(localPath.getParent)
          saveImage(image1, localPath)
          imageCache.put(memCacheKey, image1)
        }
        }
    }
  }

  def saveImage(image: Image, path: Path): Unit = {
    Objects.requireNonNull(image)
    Objects.requireNonNull(path)
    val bufferImage = SwingFXUtils.fromFXImage(image, null)
    if (bufferImage == null) return
    try
      ImageIO.write(bufferImage, "jpg", path.toFile)
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  def fromDisk(path: Path): Image = {
    Objects.requireNonNull(path)
    logger.debug("Local:{}", path)
    new Image("file:" + path.toString)
  }

  def loadRemote(url: String)(callback: (Image) => Unit): Image = {
    Objects.requireNonNull(url)
    logger.debug("Remote:{}", url)
    val image = new Image(url, true)

    if (callback != null)
      image.progressProperty.addListener((_, _, newValue: Number) => {
        if (newValue != null && newValue.doubleValue == 1)
          callback(image)
      })

    image.exceptionProperty.addListener((_, _, e: Exception) => {
      if (e != null)
        if (!e.isInstanceOf[FileNotFoundException])
          AppCache.imageMemCache.remove(url)
        else
          logger.error(e.getMessage)
    })
    image
  }

  def getImageBytes(image: Image): Array[Byte] = {
    Objects.requireNonNull(image)
    val bufferImage = SwingFXUtils.fromFXImage(image, null)
    if (bufferImage == null) return null

    val stream = new ByteArrayOutputStream
    try {
      ImageIO.write(bufferImage, "jpg", stream)
      return stream.toByteArray
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      stream.close()
    }
    null
  }
}