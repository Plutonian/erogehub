package com.goexp.piplline.core

import scala.beans.BeanProperty

private[piplline]
trait MessageDriven {

  @BeanProperty
  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message) = queue.offer(mes)

  def sendTo(target: Class[_ <: MessageHandler], entity: Any) = queue.offer(Message(target, entity))

}
