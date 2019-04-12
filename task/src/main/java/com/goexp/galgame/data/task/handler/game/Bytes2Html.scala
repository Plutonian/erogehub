package com.goexp.galgame.data.task.handler.game

import java.util
import java.util.Objects

import com.goexp.common.util.WebUtil
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.client.GetChu
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class Bytes2Html extends DefaultMessageHandler[util.Map.Entry[Integer, Array[Byte]]] {
  final private val logger = LoggerFactory.getLogger(classOf[Bytes2Html])

  override def process(message: Message[util.Map.Entry[Integer, Array[Byte]]], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
    val entry = message.entity
    logger.debug("<Bytes2Html> {}", entry.getKey)

    val html = util.Map.entry(entry.getKey, Objects.requireNonNull(WebUtil.decodeGzip(entry.getValue, GetChu.DEFAULT_CHARSET)))

    msgQueue.offer(new Message[util.Map.Entry[Integer, String]](MesType.ContentHtml, html))
  }
}