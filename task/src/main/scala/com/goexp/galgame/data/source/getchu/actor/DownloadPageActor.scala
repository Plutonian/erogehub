package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.common.website.getchu.{GameList, GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.galgame.data.source.getchu.PageDownloader._
import com.goexp.pipeline.handler.OnErrorReTryActor

import java.time.LocalDate
import java.util.concurrent.TimeUnit

/**
 * Net IO
 */
class DownloadPageActor extends OnErrorReTryActor(20, 5, TimeUnit.SECONDS) {

  override def receive = {

    // download game detail page
    case gid: Int =>

      logger.debug(s"GET:GameDetail: $gid")

      val url = GetchuGameRemote.byId(gid)
      val request = RequestBuilder(url).adaltFlag.build
      val html = download(request)(DEFAULT_CHARSET)

      logger.debug(s"GET GameDetail:${gid} OK")

      sendTo[ParsePageActor]((gid, html))
      sendTo[ShutdownActor]("reset")

    // download page from date range
    case (start: LocalDate, end: LocalDate) =>

      logger.info(s"Start:$start,End:$end")

      val url = GameList.byDateRange(start, end)
      val request = RequestBuilder(url).adaltFlag.build
      val html = download(request)(DEFAULT_CHARSET)

      logger.debug(html)

      sendTo[ParsePageActor]((html, "ListPageParser"))
      sendTo[ShutdownActor]("reset")


    // download page by brand(Doujin)
    case (brandId: Int, "doujin") =>
      logger.info(s"brandId:$brandId")

      val url = GameList.byBrandDoujin(brandId)
      val request = RequestBuilder(url).adaltFlag.build
      val html = download(request)(DEFAULT_CHARSET)

      sendTo[ParsePageActor]((html, "ListPageParser"))
      sendTo[ShutdownActor]("reset")


    // download page by brand(normal)
    case (brandId: Int, "normal") =>
      logger.info(s"brandId:$brandId")

      val url = GameList.byBrand(brandId)
      val request = RequestBuilder(url).adaltFlag.build
      val html = download(request)(DEFAULT_CHARSET)

      sendTo[ParsePageActor]((html, "ListPageParser"))
      sendTo[ShutdownActor]("reset")

  }
}