package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu.GameService.Download
import org.slf4j.LoggerFactory

/**
  * Net IO
  */
class DownloadGameHandler extends DefaultMessageHandler[Int] {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadGameHandler])

  override def process(message: Message[Int]) = {
    val gid = message.entity
    logger.debug("Download {}", gid)
    try {
      val zipBytes = Download.getBytes(gid)
      logger.debug("Download OK:{}", gid)

      send(Message[(Int, Array[Byte])](MesType.ContentBytes, (gid, zipBytes)))
    }
    catch {
      case e: Exception =>
        logger.error("Re-down:{} IOException:{}", gid, e.getMessage)
        send(Message[Int](MesType.NEED_DOWN_GAME, gid))

    }

  }
}