package com.goexp.galgame.data.source.getchu.task.handler

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.CompletionException

import com.goexp.galgame.common.util.ImageUtil
import com.goexp.galgame.common.util.ImageUtil.{ErrorCodeException, FileIsNotImageException}
import com.goexp.piplline.handler.DefaultHandler
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

class DownloadImage extends DefaultHandler {
  final private val logger = Logger(classOf[DownloadImage])


  override def processEntity: PartialFunction[Any, Unit] = {
    case (local: Path, remote: String) =>
      def rPath(path: Path) =
        path.iterator().asScala.to(LazyList).takeRight(2).mkString("/")


      val showLocal = rPath(local)

      logger.info(s"Downloading...  ${showLocal} --> $remote")

      ImageUtil.loadFromAsyn(remote)
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
