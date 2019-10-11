package com.goexp.galgame.common.util

import java.net.http.HttpResponse.BodySubscribers
import java.net.http.{HttpRequest, HttpResponse}

import com.goexp.common.util.web.HttpUtil
import com.goexp.common.util.web.url._
import org.slf4j.LoggerFactory

object ImageUtil {

  private val logger = LoggerFactory.getLogger(ImageUtil.getClass)

  def loadFrom(url: String): Array[Byte] = {
    loadFromAsyn(url).join().body()
  }

  def loadFromAsyn(url: String) = {
    logger.debug("Url:{}", url)

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

    val handler: HttpResponse.BodyHandler[Array[Byte]] = reponseInfo => {
      val code = reponseInfo.statusCode()
      if (code != 200)
        throw new RuntimeException(s"Error code:${code}")
      else {
        val headers = reponseInfo.headers()

        if (!headers.firstValue("content-type").map[Boolean](s => s.equals("image/jpeg")).orElse(false))
          throw new RuntimeException("Body is not image")
        else
          BodySubscribers.ofByteArray()
      }
    }

    //    val handler = BodyHandlers.ofByteArray()

    HttpUtil.httpClient.sendAsync(request, handler)

  }

}
