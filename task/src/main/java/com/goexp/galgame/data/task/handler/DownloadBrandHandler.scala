package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu
import org.slf4j.LoggerFactory

class DownloadBrandHandler extends DefaultMessageHandler[Int] {
  final private val logger = LoggerFactory.getLogger(classOf[DownloadBrandHandler])

  override def process(message: Message[Int], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
    val id = message.entity
    logger.info("Down:{}", id)
    try GetChu.BrandService.download(id)
    catch {
      case e: Exception =>
        logger.error("ReDown:{}", id)
        msgQueue.offer(new Message[Int](99, id))
    }
    msgQueue.offer(new Message[Int](MesType.Brand, id))
  }
}