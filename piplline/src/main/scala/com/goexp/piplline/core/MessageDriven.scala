package com.goexp.piplline.core

import scala.beans.BeanProperty

trait MessageDriven {

  @BeanProperty
  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message) = queue.offer(mes)

  def send(code: Int, entity: Any) = queue.offer(Message(code, entity))

  def send(clazz: Class[_], entity: Any) = queue.offer(new Message(clazz, entity))

  //  def sendTo[T <: MessageHandler](entity: Any) = queue.offer(Message(classOf[T].hashCode(), entity))
}
