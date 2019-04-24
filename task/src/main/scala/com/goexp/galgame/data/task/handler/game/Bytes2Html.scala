package com.goexp.galgame.data.task.handler.game

import java.util.Objects

import com.goexp.common.util.WebUtil
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class Bytes2Html extends DefaultMessageHandler[(Int, Array[Byte])] {
  final private val logger = LoggerFactory.getLogger(classOf[Bytes2Html])

  override def process(message: Message[(Int, Array[Byte])]) = {
    val (id, bytes) = message.entity

    logger.debug("<Bytes2Html> {}", id)

    val html = (id, Objects.requireNonNull(WebUtil.decodeGzip(bytes, GetChu.DEFAULT_CHARSET)))

    send(new Message[(Int, String)](MesType.ContentHtml, html))
  }
}