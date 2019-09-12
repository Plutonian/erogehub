package com.goexp.galgame.gui.util.res.gameimg

import java.io.{ByteArrayInputStream, IOException}
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.{Files, Path}
import java.util.Objects
import java.util.concurrent.{Executors, ThreadFactory}

import com.goexp.common.util.web.HttpUtil
import com.goexp.common.util.web.url._
import com.goexp.galgame.gui.Config.IMG_PATH
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.cache.AppCache
import javafx.application.Platform
import javafx.scene.image.{Image, WritableImage}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

object GameImages {
  private val logger = LoggerFactory.getLogger(this.getClass)

  // thread pool
  val executers = Executors.newFixedThreadPool(30, new ThreadFactory {
    override def newThread(r: Runnable): Thread = {
      val thread = new Thread(r)
      thread.setPriority(Thread.MIN_PRIORITY)
      thread.setDaemon(true)
      thread
    }
  })

  def loadFrom(url: String): Array[Byte] = {

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


    def saveImage(bytes: Array[Byte], path: Path): Unit = {
      Objects.requireNonNull(bytes)
      Objects.requireNonNull(path)
      try
        Files.write(path, bytes)
      catch {
        case e: IOException =>
          println(game)
          e.printStackTrace()
      }
    }

    logger.trace("LocalKey={},memCacheKey={}", diskCacheKey, memCacheKey)
    val imageCache = AppCache.imageMemCache
    //try heat cache

    imageCache.get(memCacheKey) match {
      case Some(img) => onOK(img)
      case None =>
        val localPath = IMG_PATH.resolve(diskCacheKey + ".jpg")
        logger.trace("localPath={}", localPath)

        //heat disk cache or load from remote
        if (Files.exists(localPath)) {
          val image = new Image("file:" + localPath.toString)
          imageCache.put(memCacheKey, image)
          onOK(image)
        } else {
          //placeholder
          AppCache.imageMemCache.put(memCacheKey, new WritableImage(1, 1))


          implicit val executor = ExecutionContext.fromExecutor(executers)

          Future {
            loadFrom(memCacheKey)
          }
            .map(bytes => {
              val img = new Image(new ByteArrayInputStream(bytes))
              Platform.runLater(() => {
                imageCache.put(memCacheKey, img)
                onOK(img)

                if (game.isOkState) {
                  //Save anys
                  Future {
                    Files.createDirectories(localPath.getParent)
                    saveImage(bytes, localPath)

                  }
                }
              })
            })
            .onComplete {
              case Failure(e) =>
                Platform.runLater(() => {
                  imageCache.remove(memCacheKey)
                })
                logger.error(e.getMessage)
            }
        }
    }

  }
}

