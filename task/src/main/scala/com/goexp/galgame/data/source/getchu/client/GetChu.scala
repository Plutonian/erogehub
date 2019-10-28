package com.goexp.galgame.data.source.getchu.client

import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofByteArray
import java.time.LocalDate

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.galgame.common.util.LimitHttpClient
import com.goexp.galgame.common.website.getchu.{GameList, RequestBuilder}
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.source.getchu.parser.GetchuBrandParser
import com.goexp.galgame.data.source.getchu.parser.game.ListPageParser
import org.slf4j.LoggerFactory

object GetChu {
  private val logger = LoggerFactory.getLogger(GetChu.getClass)

  val DEFAULT_CHARSET = "EUC-JP"


  def getHtml(request: HttpRequest): String = {

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
      .exceptionally { e =>
        logger.error(s"${e.getCause.getMessage}", e.getCause)
        throw e.getCause
        //        null
      }
      .join()
  }

  def getHtmlAsy(request: HttpRequest) = {

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

  //  Content-Encoding: gzip

  object GameRemote {

    def from(brandId: Int): LazyList[Game] = {

      val request = RequestBuilder(GameList.byBrand(brandId)).adaltFlag.build
      val html = getHtml(request)

      new ListPageParser().parse(html)
    }

    def from(from: LocalDate, to: LocalDate): LazyList[Game] = {
      val url = GameList.byDateRange(from, to)
      val request = RequestBuilder(url).adaltFlag.build

      logger.debug(url)

      val html = getHtml(request)

      new ListPageParser().parse(html)
    }

    //from local
    def from(bytes: Array[Byte]): LazyList[Game] = {
      val html = bytes.unGzip().decode(DEFAULT_CHARSET)
      new ListPageParser().parse(html)
    }
  }

  object BrandService {

    def all(): LazyList[Brand] = {

      val request = RequestBuilder("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
      val html = getHtml(request)

      logger.debug(html)

      new GetchuBrandParser().parse(html)

    }
  }

}