package com.goexp.galgame.data.source.getchu.task.handler

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.CompletionException

import com.goexp.galgame.common.util.ImageUtil
import com.goexp.galgame.common.util.ImageUtil.{ErrorCodeException, FileIsNotImageException}
import com.goexp.piplline.handler.DefaultHandler
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

class DownloadImage extends DefaultHandler {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadImage])


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
            logger.debug("{}", ex)

            ex.getCause match {
              case _: HttpTimeoutException =>
                logger.error(s"RequestTimeout")
              case _: ConnectException =>
                logger.error(s"CannotConnect")
              case e: IOException =>
                logger.error(s"ConnectionReset")
                logger.debug("IOException", e)
              case ErrorCodeException(errorCode) =>
                logger.error(s"Response Error:code={}", errorCode)
              case _: FileIsNotImageException =>
                logger.error(s"Not Image")
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
