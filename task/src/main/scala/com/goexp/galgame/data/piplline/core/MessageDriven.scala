package com.goexp.galgame.data.piplline.core

import scala.beans.BeanProperty

trait MessageDriven {

  @BeanProperty
  var queue: MessageQueueProxy[Message[_]] = _

//  def setQueue(queue: MessageQueueProxy[Message[_]]): Unit = this.queue = queue

  def send(mes: Message[_]) = queue.offer(mes)
}
