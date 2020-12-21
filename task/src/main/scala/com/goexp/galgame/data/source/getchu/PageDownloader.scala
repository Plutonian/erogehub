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

  val client = new LimitHttpClient(20, 20, TimeUnit.SECONDS)

  implicit val DEFAULT_CHARSET = StandardCharsets.UTF_8

  def download(request: HttpRequest)(implicit charset: Charset): String = {
    downloadAnsyn(request)(charset)
      .join()
  }

  def downloadAnsyn(request: HttpRequest)(implicit charset: Charset): CompletableFuture[String] = {

    client.sendAsync(request, ofByteArray)
      .thenApply[String] { res =>
        val bytes = res.body()

        val isGzip = res.headers().firstValue("content-encoding")
          .map[Boolean] {
            _ == "gzip"
          }.orElse(false)

        val rawBytes = if (isGzip) bytes.unGzip() else bytes

        rawBytes.decode(charset)

      }
    //      .exceptionally { e =>
    //        logger.error(s"${e.getCause.getMessage}", e.getCause)
    //        throw e.getCause
    //        //        null
    //      }
  }
}