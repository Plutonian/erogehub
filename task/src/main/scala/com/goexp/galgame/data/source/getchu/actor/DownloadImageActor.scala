package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.source.getchu.ImageDownloader
import com.goexp.galgame.data.source.getchu.ImageDownloader.{ErrorCodeException, FileIsNotImageException}
import com.goexp.galgame.data.source.getchu.actor.DownloadImageActor.{ImageParam, allCount, errorCount, finishCount}
import com.goexp.pipeline.handler.DefaultActor

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.CompletionException
import java.util.concurrent.atomic.AtomicInteger
import scala.jdk.CollectionConverters._


object DownloadImageActor {

  case class ImageParam(local: Path, remote: String)

  val allCount = new AtomicInteger(0)
  val finishCount = new AtomicInteger(0)
  val errorCount = new AtomicInteger(0)


}

class DownloadImageActor extends DefaultActor {

  override def receive = {
    case ImageParam(local, remote) =>
      def rPath(path: Path) =
        path.iterator().asScala.to(LazyList).takeRight(2).mkString("/")


      val showLocal = rPath(local)

      logger.info(s"Downloading...  ${showLocal}")
      logger.debug(s"Downloading...  ${showLocal} --> $remote")

      ImageDownloader.downloadAnsyn(remote)
        .thenApply[Array[Byte]] { res => res.body() }
        .thenAccept { bytes =>
          finishCount.incrementAndGet()

          Files.createDirectories(local.getParent)
          Files.write(local, bytes)

          logger.info(s"Success!!! ${showLocal} (${bytes.length})   (${finishCount.get()}/${allCount.get()}) ")
          logger.debug(s"Success!!! ${showLocal} (${bytes.length})   (${finishCount.get()}/${allCount.get()}) Error:${errorCount.get()}")

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
                logger.warn(s"IOException ${e.getClass.getName} ${e.getMessage}")
              case ErrorCodeException(errorCode) =>
                logger.warn(s"Response Error:code=${errorCode}")
              case _: FileIsNotImageException =>
                logger.warn(s"Not Image")
              case e =>
                logger.error(s"NoneCatchExecption")
                e.printStackTrace()
            }

            errorCount.incrementAndGet()
            null

          case _ =>
            logger.error(s"NoneCatchExecption")
            null
        }
        .join()

      sendTo[ShutdownActor]("reset")

  }

}
