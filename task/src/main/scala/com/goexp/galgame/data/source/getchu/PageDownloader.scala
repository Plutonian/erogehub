package com.goexp.galgame.data.source.getchu

import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofByteArray
import java.nio.charset.{Charset, StandardCharsets}
import java.util.concurrent.{CompletableFuture, TimeUnit}

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.galgame.data.ansyn.LimitHttpClient
import com.typesafe.scalalogging.Logger

object PageDownloader {

  private val logger = Logger(PageDownloader.getClass)

  implicit val DEFAULT_CHARSET = StandardCharsets.UTF_8

  def download(request: HttpRequest)(implicit charset: Charset): String = {
    downloadAnsyn(request)(charset)
      .join()
  }

  def downloadAnsyn(request: HttpRequest)(implicit charset: Charset): CompletableFuture[String] = {

    logger.debug(s"request HEADER:${request.headers()}")

    LimitHttpClient().sendAsync(request, ofByteArray)
      .thenApply[String] { res =>
        val bytes = res.body()

        val isGzip = res.headers().firstValue("content-encoding")
          .map[Boolean] {
            _ == "gzip"
          }.orElse(false)


        logger.debug(s"response HEADER:${res.headers()}")

        if (bytes.isEmpty) {
          logger.error(s"Empty page: ${request.uri()}")


          ""
        } else {

          val rawBytes = if (isGzip) bytes.unGzip() else bytes

          rawBytes.decode(charset)
        }

      }
    //      .exceptionally { e =>
    //        logger.error(s"${e.getCause.getMessage}", e.getCause)
    //        throw e.getCause
    //        //        null
    //      }
  }
}