package com.goexp.galgame.data.source.getchu.task.handler

import com.goexp.galgame.common.website.getchu.{GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.Client
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.galgame.data.source.getchu.task.handler.game.Html2GameOK
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

/**
  * Net IO
  */
class DownloadGameHandler extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadGameHandler])

  private[this] def getBytes(gameId: Int): String = {
    logger.debug(s"Download:Game: $gameId")

    val request = RequestBuilder(GetchuGameRemote.byId(gameId)).adaltFlag.build

    Client.getHtml(request)

  }

  override def process(message: Message) = {
    message.entity match {

      case gid: Int =>
        try {
          val html = getBytes(gid)
          logger.debug("Download OK:{}", gid)

          sendTo(classOf[Html2GameOK], (gid, html))
        }
        catch {
          case e: Exception =>
            logger.error("Re-down:{} IOException:{}", gid, e.getMessage)
            sendTo(classOf[DownloadGameHandler], gid)

        }
    }

    //    val (gid: Int) = message.entity

  }
}