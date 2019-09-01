package com.goexp.galgame.data.piplline.core

import scala.beans.BeanProperty

trait MessageDriven {

  @BeanProperty
  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message) = queue.offer(mes)
}
