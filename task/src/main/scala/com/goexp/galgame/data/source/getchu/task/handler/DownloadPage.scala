package com.goexp.galgame.data.source.getchu.task.handler

import java.time.LocalDate
import java.util.concurrent.TimeUnit

import com.goexp.galgame.common.website.getchu.{GameList, GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.source.getchu.Client._
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.piplline.handler.OnErrorReTryActor

/**
 * Net IO
 */
class DownloadPage extends OnErrorReTryActor(20, 5, TimeUnit.SECONDS) {

  override def receive = {

    // download game detail page
    case gid: Int =>

      logger.trace(s"Download:Game: $gid")

      val url = GetchuGameRemote.byId(gid)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      logger.trace(s"Download OK:${gid}")

      sendTo[ParsePage]((gid, html))

    // download page from date range
    case (start: LocalDate, end: LocalDate) =>

      logger.info(s"Start:$start,End:$end")

      val url = GameList.byDateRange(start, end)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      sendTo[ParsePage]((html, "ListPageParser"))

    // download page by brand(Doujin)
    case (brandId: Int, "doujin") =>
      logger.info(s"brandId:$brandId")

      val url = GameList.byBrandDoujin(brandId)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      sendTo[ParsePage]((html, "ListPageParser"))

    // download page by brand(normal)
    case (brandId: Int, "normal") =>
      logger.info(s"brandId:$brandId")

      val url = GameList.byBrand(brandId)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      sendTo[ParsePage]((html, "ListPageParser"))
  }
}