package com.goexp.galgame.data.task.client

import java.io.IOException
import java.net.http.HttpResponse.BodyHandlers.{ofByteArray, ofFile}
import java.net.http.{HttpRequest, HttpResponse}
import java.nio.charset.Charset
import java.nio.file.{Files, Path, StandardCopyOption, StandardOpenOption}
import java.time.LocalDate

import com.goexp.common.util.WebUtil
import com.goexp.galgame.common.website.GetchuURL
import com.goexp.galgame.data.Config
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.ParseException
import com.goexp.galgame.data.parser.game.{DetailPageParser, ListPageParser}
import org.slf4j.LoggerFactory

object GetChu {
  val DEFAULT_CHARSET: Charset = Charset.forName("EUC-JP")
  private val logger = LoggerFactory.getLogger(GetChu.getClass)

  def getHtml(request: HttpRequest): String = {
    try {
      val bytes = WebUtil.httpClient.send(request, ofByteArray).body
      return WebUtil.decodeGzip(bytes, DEFAULT_CHARSET)
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
      WebUtil.httpClient.send(GetchuURL.RequestBuilder.create(GetchuURL.getFromId(gameId)).adaltFlag.build, ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
      Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING)
    }

    @throws[ParseException]
    def getFrom(gameId: Int, html: String): Game = {
      val parser = new DetailPageParser
      try parser.parse(gameId, html)
      catch {
        case e: Exception =>
          throw new ParseException
      }
    }
  }

  object BrandService {
    def gamesFrom(brandId: Int): Stream[Game] = {
      try {
        val bytes = WebUtil.httpClient.send(GetchuURL.RequestBuilder.create(GetchuURL.getListByBrand(brandId)).adaltFlag.build, ofByteArray).asInstanceOf[HttpResponse[_]].body.asInstanceOf[Array[Byte]]
        val html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET)
        new ListPageParser().parse(html)
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
          logger.error(s"<error> BrandId:$brandId")
      }
      null
    }

    def gamesFrom(from: LocalDate, to: LocalDate): Stream[Game] = {
      try {

        val request = GetchuURL.RequestBuilder.create(GetchuURL.gameListFromDateRange(from, to)).adaltFlag.build

        val bytes = WebUtil.httpClient.send(request, ofByteArray).body

        val html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET)
        return new ListPageParser().parse(html)
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
          logger.error(s"<error> From:$from to:$to")
      }
      null
    }

    def gamesFrom(bytes: Array[Byte]): Stream[Game] = {
      val html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET)
      new ListPageParser().parse(html)
    }

    @throws[IOException]
    @throws[InterruptedException]
    def download(brandId: Int) =
      try {
        val localPath = Config.BRAND_CACHE_ROOT.resolve(s"$brandId.bytes")
        val tempPath = Path.of(s"${localPath}_")

        val request = GetchuURL.RequestBuilder.create(GetchuURL.getListByBrand(brandId)).adaltFlag.build

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