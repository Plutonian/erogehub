package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu
import org.slf4j.LoggerFactory

class DownloadGameHandler extends DefaultMessageHandler[Int] {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadGameHandler])

  override def process(message: Message[Int]) = {
    val gid = message.entity
    logger.debug("Download {}", gid)
    try GetChu.GameService.download(gid)
    catch {
      case e: Exception =>
        logger.error("Re-down:{}", gid)
        send(new Message[Int](MesType.NEED_DOWN_GAME, gid))
    }
    send(new Message[Int](MesType.Game, gid))
  }
}