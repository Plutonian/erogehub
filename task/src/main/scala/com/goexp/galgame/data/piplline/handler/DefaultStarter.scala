package com.goexp.galgame.data.piplline.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}

abstract class DefaultStarter extends Starter {
  var queue: MessageQueueProxy[Message[_]] = _

  def setQueue(queue: MessageQueueProxy[Message[_]]): Unit = this.queue = queue

  def send(mes: Message[_]) = queue.offer(mes)
}