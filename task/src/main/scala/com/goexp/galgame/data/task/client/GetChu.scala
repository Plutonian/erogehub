package com.goexp.galgame.data.task.client

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofByteArray
import java.time.LocalDate

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.common.util.web.HttpUtil
import com.goexp.galgame.common.website.getchu.{GetchuGameRemote => GameUrl, _}
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.parser.GetchuBrandParser
import com.goexp.galgame.data.parser.game.ListPageParser
import org.slf4j.LoggerFactory

object GetChu {
  val DEFAULT_CHARSET = "EUC-JP"
  private val logger = LoggerFactory.getLogger(GetChu.getClass)

  def getHtml(request: HttpRequest): String = {
    try {
      val bytes = HttpUtil.httpClient.send(request, ofByteArray).body
      return bytes.unGzip().decode(DEFAULT_CHARSET)
    } catch {
      case e@(_: InterruptedException | _: IOException) =>
        e.printStackTrace()
    }
    null
  }

  object GameRemote {

    object Download {

      @throws[IOException]
      def getBytes(gameId: Int): Array[Byte] = {
        logger.debug(s"Download:Game: $gameId")
        val request = RequestBuilder(GameUrl.byId(gameId)).adaltFlag.build
        val response = HttpUtil.httpClient.send(request, ofByteArray())

        logger.debug("Id:{}\t\tRes code:{}", gameId, response.statusCode())

        response.body()
      }
    }


    def from(brandId: Int): LazyList[Game] = {
      try {
        val request = RequestBuilder(GameList.byBrand(brandId)).adaltFlag.build
        val bytes = HttpUtil.httpClient.send(request, ofByteArray).body
        val html = bytes.unGzip().decode(DEFAULT_CHARSET)
        new ListPageParser().parse(html)
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
          logger.error(s"<error> BrandId:$brandId")
      }
      null
    }

    def from(from: LocalDate, to: LocalDate): LazyList[Game] = {
      try {

        val request = RequestBuilder(GameList.byDateRange(from, to)).adaltFlag.build

        val bytes = HttpUtil.httpClient.send(request, ofByteArray).body

        val html = bytes.unGzip().decode(DEFAULT_CHARSET)
        return new ListPageParser().parse(html)
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
          logger.error(s"<error> From:$from to:$to")
      }
      null
    }

    def from(bytes: Array[Byte]): LazyList[Game] = {
      val html = bytes.unGzip().decode(DEFAULT_CHARSET)
      new ListPageParser().parse(html)
    }
  }

  object BrandService {

    def all(): LazyList[Brand] = {

      val request = RequestBuilder("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
      val html = GetChu.getHtml(request)

      new GetchuBrandParser().parse(html)

    }
  }

}