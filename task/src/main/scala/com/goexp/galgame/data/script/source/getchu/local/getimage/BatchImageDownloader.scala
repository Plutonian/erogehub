package com.goexp.galgame.data.script.source.getchu.local.getimage

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{CompletionException, CountDownLatch}

import com.goexp.common.util.Logger
import com.goexp.galgame.data.source.getchu.ImageDownloader.{ErrorCodeException, FileIsNotImageException}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.ImageDownloader

import scala.jdk.CollectionConverters._

object BatchImageDownloader extends Logger {

  def download(games: LazyList[Game]) = {
    val imgs = games
      .flatMap { g =>
        g.allImgs
      }

    var requestNum = 0
    val hopeDownload = imgs.size

    logger.info(s"Need download:${hopeDownload}")

    val responseNum = new AtomicInteger(0)

    val allDownLatch = new CountDownLatch(hopeDownload)

    imgs.foreach {
      case (local: Path, remote: String) =>
        def rPath(path: Path) =
          path.iterator().asScala.to(LazyList).takeRight(2).mkString("/")


        val showLocal = rPath(local)

        requestNum += 1
        logger.info(s"Downloading... [$requestNum/$hopeDownload] ${showLocal} --> $remote")

        ImageDownloader.downloadAsyn(remote)
          .thenApply[Array[Byte]] { res => res.body() }
          .thenAccept { bytes =>
            Files.createDirectories(local.getParent)
            Files.write(local, bytes)

            logger.info(s"Success!!! [${responseNum.incrementAndGet()}/$hopeDownload] ${showLocal} (${bytes.length})")

            allDownLatch.countDown()

          }
          .exceptionally {
            case ex: CompletionException =>
              logger.debug("", ex)

              ex.getCause match {
                case _: HttpTimeoutException =>
                  logger.warn(s"RequestTimeout")
                case _: ConnectException =>
                  logger.warn(s"CannotConnect")
                case e: IOException =>
                  logger.warn(s"ConnectionReset")
                case ErrorCodeException(errorCode) =>
                  logger.warn(s"Response Error:code=${errorCode}")
                case _: FileIsNotImageException =>
                  logger.warn(s"Not Image")
                case e =>
                  logger.error(s"NoneCatchExecption", e)
              }
              allDownLatch.countDown()
              null

            case _ =>
              logger.error(s"NoneCatchExecption")
              allDownLatch.countDown()
              null
          }
    }

    allDownLatch.await()

    logger.info("Download complete")
  }
}
