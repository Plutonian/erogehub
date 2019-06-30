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
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.ParseException
import com.goexp.galgame.data.parser.game.{DetailPageParser, ListPageParser}
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

  }

  object BrandService {
    def gamesFrom(brandId: Int): LazyList[Game] = {
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

    def gamesFrom(from: LocalDate, to: LocalDate): LazyList[Game] = {
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

    def gamesFrom(bytes: Array[Byte]): LazyList[Game] = {
      val html = Gzip.decode(bytes, DEFAULT_CHARSET)
      new ListPageParser().parse(html)
    }

    @throws[IOException]
    @throws[InterruptedException]
    def download(brandId: Int) =
      try {
        val localPath = Config.BRAND_CACHE_ROOT.resolve(s"$brandId.bytes")
        val tempPath = Path.of(s"${localPath}_")

        val request = GetchuURL.RequestBuilder.create(GameList.byBrand(brandId)).adaltFlag.build

        WebUtil.httpClient.send(request, ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
        Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING)
        logger.debug(s"Download: Brand:$brandId")
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          logger.error(s"<Brand> $brandId Mes:${e.getMessage}")
          throw e
      }
  }

}