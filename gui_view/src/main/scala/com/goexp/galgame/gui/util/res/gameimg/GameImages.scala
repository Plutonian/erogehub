package com.goexp.galgame.gui.util.res.gameimg

import java.io.{ByteArrayInputStream, IOException}
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.{Files, Path}
import java.util.Objects
import java.util.concurrent.Executors

import com.goexp.common.util.web.HttpUtil
import com.goexp.common.util.web.url._
import com.goexp.galgame.gui.Config.IMG_PATH
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.{Image, WritableImage}
import javax.imageio.ImageIO
import org.slf4j.LoggerFactory

object GameImages {
  private val logger = LoggerFactory.getLogger(this.getClass)

  // thread pool
  val executers = Executors.newFixedThreadPool(30)

  def load(url: String): Array[Byte] = {

    val request: HttpRequest = HttpRequest.newBuilder.uri(url)
      .header("Cookie", "getchu_adalt_flag=getchu.com")
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      //                .header("Accept", "image/webp,*/*")
      .header("Accept-Encoding", "gzip, deflate")
      .header("Accept-Language", "ja,en-US;q=0.7,en;q=0.3")
      .header("Cache-Control", "max-age=0")
      .header("Upgrade-Insecure-Requests", "1")
      .header("Referer", "http://www.getchu.com")
      .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
      .build()

    val res = HttpUtil.httpClient.send(request, BodyHandlers.ofByteArray())
    res.body()

  }

  def get(game: Game)(diskCacheKey: String, memCacheKey: String)(onOK: (Image) => Unit) = {
    Objects.requireNonNull(diskCacheKey)
    Objects.requireNonNull(memCacheKey)
    Objects.requireNonNull(onOK, "onOK must init")


    def loadFromRemote(url: String)(onLoadOK: (Image) => Unit, onLoadError: () => Unit) = {
      Objects.requireNonNull(url)
      logger.debug("Remote:{}", url)

      //placeholder
      AppCache.imageMemCache.put(memCacheKey, new WritableImage(1, 1))

      executers.submit(new Runnable {
        override def run(): Unit = {

          try {
            val bytes = load(url)

            Platform.runLater(() => {
              onLoadOK(new Image(new ByteArrayInputStream(bytes)))
            })
          }
          catch {
            case e: IOException =>
              onLoadError()
              e.printStackTrace()
          }
        }
      })
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
          println(game)
          e.printStackTrace()
      }
    }

    logger.debug("LocalKey={},memCacheKey={}", diskCacheKey, memCacheKey)
    val imageCache = AppCache.imageMemCache
    //try heat cache

    imageCache.get(memCacheKey) match {
      case Some(img) => onOK(img)
      case None =>
        val localPath = IMG_PATH.resolve(diskCacheKey + ".jpg")
        logger.debug("localPath={}", localPath)

        //heat disk cache or load from remote
        Files.exists(localPath) match {
          //load from disk
          case true =>
            val image = new Image("file:" + localPath.toString)
            imageCache.put(memCacheKey, image)
            onOK(image)
          //load from remote
          case false =>
            if (game.isOkState) {
              //cache to disk
              loadFromRemote(memCacheKey)(
                onLoadOK = img => {
                  imageCache.put(memCacheKey, img)
                  onOK(img)

                  Files.createDirectories(localPath.getParent)
                  saveImage(img, localPath)
                }
                , onLoadError = () => {
                })
            } else {
              //without save
              loadFromRemote(memCacheKey)(
                onLoadOK = img => {
                  imageCache.put(memCacheKey, img)
                  onOK(img)
                }
                , onLoadError = () => {
                })
            }
        }
    }

  }
}

