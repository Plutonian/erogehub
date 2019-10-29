package com.goexp.galgame.data

import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofByteArray
import java.nio.charset.{Charset, StandardCharsets}
import java.util.concurrent.CompletableFuture

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.galgame.common.util.LimitHttpClient
import org.slf4j.LoggerFactory

object Client {

  private val logger = LoggerFactory.getLogger(Client.getClass)

  implicit val DEFAULT_CHARSET = StandardCharsets.UTF_8

  def getHtml(request: HttpRequest)(implicit charset: Charset): String = {
    getHtmlAsy(request)(charset)
      .join()
  }

  def getHtmlAsy(request: HttpRequest)(implicit charset: Charset): CompletableFuture[String] = {

    LimitHttpClient().sendAsync(request, ofByteArray)
      .thenApply[String] { res =>
        val bytes = res.body()

        val isGzip = res.headers().firstValue("content-encoding")
          .map[Boolean] {
            _ == "gzip"
          }.orElse(false)

        val rawBytes = if (isGzip) bytes.unGzip() else bytes

        rawBytes.decode(DEFAULT_CHARSET)

      }
    //      .exceptionally { e =>
    //        logger.error(s"${e.getCause.getMessage}", e.getCause)
    //        throw e.getCause
    //        //        null
    //      }
  }
}