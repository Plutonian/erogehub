package com.goexp.galgame.data.source.getchu.task.handler

import java.util.concurrent.TimeUnit

import com.goexp.galgame.common.website.getchu.{GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.Client
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.galgame.data.source.getchu.task.handler.game.Html2GameOK
import com.goexp.piplline.handler.OnErrorReTryHandler
import org.slf4j.LoggerFactory

/**
  * Net IO
  */
class DownloadGameHandler extends OnErrorReTryHandler(20, 5, TimeUnit.SECONDS) {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadGameHandler])

  private[this] def getBytes(gameId: Int): String = {
    logger.debug(s"Download:Game: $gameId")

    val request = RequestBuilder(GetchuGameRemote.byId(gameId)).adaltFlag.build

    Client.getHtml(request)

  }

  override def processEntity: PartialFunction[Any, Unit] = {
    case gid: Int =>

      val html = getBytes(gid)
      logger.debug("Download OK:{}", gid)

      sendTo(classOf[Html2GameOK], (gid, html))

  }
}