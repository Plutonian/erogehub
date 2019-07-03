package com.goexp.galgame.data.task.client

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.{ofByteArray, ofFile}
import java.nio.charset.Charset
import java.nio.file.{Files, Path, StandardCopyOption, StandardOpenOption}
import java.time.LocalDate

import com.goexp.common.util.{Gzip, WebUtil}
import com.goexp.galgame.common.website.GetchuURL
import com.goexp.galgame.common.website.GetchuURL.{GameList, Game => GameUrl}
import com.goexp.galgame.data.Config
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.parser.GetchuBrandParser
import com.goexp.galgame.data.parser.game.ListPageParser
import org.slf4j.LoggerFactory

object GetChu {
  lazy val DEFAULT_CHARSET: Charset = Charset.forName("EUC-JP")
  private val logger = LoggerFactory.getLogger(GetChu.getClass)

  def getHtml(request: HttpRequest): String = {
    try {
      val bytes = WebUtil.httpClient.send(request, ofByteArray).body
      return Gzip.decode(bytes, DEFAULT_CHARSET)
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
      logger.debug("Download:Game: ${}", gameId)
      val request = GetchuURL.RequestBuilder.create(GameUrl.byId(gameId)).adaltFlag.build
      WebUtil.httpClient.send(request, ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
      Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING)
    }

    def from(brandId: Int): LazyList[Game] = {
      try {
        val request = GetchuURL.RequestBuilder.create(GameList.byBrand(brandId)).adaltFlag.build
        val bytes = WebUtil.httpClient.send(request, ofByteArray).body
        val html = Gzip.decode(bytes, DEFAULT_CHARSET)
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

        val request = GetchuURL.RequestBuilder.create(GameList.byDateRange(from, to)).adaltFlag.build

        val bytes = WebUtil.httpClient.send(request, ofByteArray).body

        val html = Gzip.decode(bytes, DEFAULT_CHARSET)
        return new ListPageParser().parse(html)
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
          logger.error(s"<error> From:$from to:$to")
      }
      null
    }

    def from(bytes: Array[Byte]): LazyList[Game] = {
      val html = Gzip.decode(bytes, DEFAULT_CHARSET)
      new ListPageParser().parse(html)
    }
  }

  object BrandService {

    def all(): LazyList[Brand] = {

      val request = GetchuURL.RequestBuilder.create("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
      val html = GetChu.getHtml(request)

      new GetchuBrandParser().parse(html)

    }
  }

}