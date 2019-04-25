package com.goexp.galgame.data.piplline.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageHandler, MessageQueueProxy}

abstract class DefaultMessageHandler[In] extends MessageHandler[In] {
  var queue: MessageQueueProxy[Message[_]] = _

  final override def setQueue(queue: MessageQueueProxy[Message[_]]): Unit = this.queue = queue

  final protected def send(mes: Message[_]) = queue.offer(mes)
}