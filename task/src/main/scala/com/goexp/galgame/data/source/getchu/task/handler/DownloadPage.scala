package com.goexp.galgame.data.source.getchu.task.handler

import java.time.LocalDate
import java.util.concurrent.TimeUnit

import com.goexp.galgame.common.website.getchu.{GameList, GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.Client._
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.piplline.handler.OnErrorReTryHandler
import org.slf4j.LoggerFactory

/**
  * Net IO
  */
class DownloadPage extends OnErrorReTryHandler(20, 5, TimeUnit.SECONDS) {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadPage])

  override def processEntity: PartialFunction[Any, Unit] = {

    // download game detail page
    case gid: Int =>

      logger.debug(s"Download:Game: $gid")

      val url = GetchuGameRemote.byId(gid)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      logger.debug("Download OK:{}", gid)

      sendTo(classOf[ParsePage], (gid, html))

    // download page from date range
    case (start: LocalDate, end: LocalDate) =>

      logger.info(s"Start:$start,End:$end")

      val url = GameList.byDateRange(start, end)
      val request = RequestBuilder(url).adaltFlag.build
      val html = getHtml(request)

      sendTo(classOf[ParsePage], (html, "ListPageParser"))
  }
}