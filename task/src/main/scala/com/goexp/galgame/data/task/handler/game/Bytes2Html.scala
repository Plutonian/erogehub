package com.goexp.galgame.data.task.handler.game

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu.DEFAULT_CHARSET
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

/**
  * unzip  && decode
  */
class Bytes2Html extends DefaultMessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[Bytes2Html])

  override def process(message: Message) = {
    message.entity match {
      case (id: Int, bytes: Array[Byte]) =>
        logger.debug("<Bytes2Html> {}", id)

        val html = bytes.unGzip().decode(DEFAULT_CHARSET)

        send(Message(MesType.ContentHtml, (id, html)))
    }
  }
}