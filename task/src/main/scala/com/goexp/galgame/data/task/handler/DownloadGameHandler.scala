package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageHandler}
import com.goexp.galgame.data.task.client.GetChu.GameService.Download
import com.goexp.galgame.data.task.handler.game.Bytes2Html
import org.slf4j.LoggerFactory

/**
  * Net IO
  */
class DownloadGameHandler extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadGameHandler])

  override def process(message: Message) = {
    message.entity match {

      case gid: Int =>
        logger.debug("Download {}", gid)
        try {
          val zipBytes = Download.getBytes(gid)
          logger.debug("Download OK:{}", gid)

          send(Message(classOf[Bytes2Html].hashCode(), (gid, zipBytes)))
        }
        catch {
          case e: Exception =>
            logger.error("Re-down:{} IOException:{}", gid, e.getMessage)
            send(Message(classOf[DownloadGameHandler].hashCode(), gid))

        }
    }

    //    val (gid: Int) = message.entity

  }
}