package com.goexp.galgame.data.source.getchu.actor

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.CompletionException

import com.goexp.galgame.data.source.getchu.ImageDownloader
import com.goexp.galgame.data.source.getchu.ImageDownloader.{ErrorCodeException, FileIsNotImageException}
import DownloadImageActor.ImageParam
import com.goexp.piplline.handler.DefaultActor

import scala.jdk.CollectionConverters._


object DownloadImageActor {

  case class ImageParam(local: Path, remote: String)

}

class DownloadImageActor extends DefaultActor {

  override def receive = {
    case ImageParam(local, remote) =>
      def rPath(path: Path) =
        path.iterator().asScala.to(LazyList).takeRight(2).mkString("/")


      val showLocal = rPath(local)

      logger.info(s"Downloading...  ${showLocal} --> $remote")

      ImageDownloader.downloadAnsyn(remote)
        .thenApply[Array[Byte]] { res => res.body() }
        .thenAccept { bytes =>
          Files.createDirectories(local.getParent)
          Files.write(local, bytes)

          logger.info(s"Success!!! ${showLocal} (${bytes.length})")

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
            null

          case _ =>
            logger.error(s"NoneCatchExecption")
            null
        }
        .join()

  }

}
