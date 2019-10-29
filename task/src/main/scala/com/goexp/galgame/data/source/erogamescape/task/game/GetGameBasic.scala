package com.goexp.galgame.data.source.erogamescape.task.game

import java.io.IOException
import java.util.concurrent.CompletionException

import com.goexp.galgame.common.website.ErogameScapeURL
import com.goexp.galgame.common.website.getchu.RequestBuilder
import com.goexp.galgame.data.Client.{DEFAULT_CHARSET, getHtml}
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

/**
  * Check game is new or already has
  */
class GetGameBasic extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[GetGameBasic])

  override def process(message: Message) = {

    message.entity match {
      case id: Int =>
        val url = ErogameScapeURL.byIdBasic(id)
        logger.debug(url)
        try {
          val html = getHtml(RequestBuilder(url).build)
          send(classOf[Html2GameBasic].hashCode(), (id, html))
        }
        catch {
          case e: CompletionException if (e.getCause.isInstanceOf[IOException]) =>
            logger.error(e.getMessage)
            send(classOf[GetGameBasic].hashCode(), id)
        }

    }
  }
}
