package com.goexp.galgame.data.task.client

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.{ofByteArray, ofFile}
import java.nio.file.{Files, Path, StandardCopyOption, StandardOpenOption}
import java.time.LocalDate

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.common.util.web.HttpUtil
import com.goexp.galgame.common.website.getchu.{GetchuGame => GameUrl, _}
import com.goexp.galgame.data.Config
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

  object GameService {
    @throws[IOException]
    @throws[InterruptedException]
    def download(gameId: Int) = {
      val localPath = Config.GAME_CACHE_ROOT.resolve(s"$gameId.bytes")
      val tempPath = Path.of(localPath.toString + "_")
      logger.debug(s"Download:Game: $gameId")
      val request = RequestBuilder(GameUrl.byId(gameId)).adaltFlag.build
      HttpUtil.httpClient.send(request, ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
      Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING)
    }

    @throws[IOException]
    def getBytes(gameId: Int): Array[Byte] = {
      //      val localPath = Config.GAME_CACHE_ROOT.resolve(s"$gameId.bytes")
      //      val tempPath = Path.of(localPath.toString + "_")
      logger.debug(s"Download:Game: $gameId")
      val request = RequestBuilder(GameUrl.byId(gameId)).adaltFlag.build
      HttpUtil.httpClient.send(request, ofByteArray()).body()
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