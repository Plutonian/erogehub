package com.goexp.piplline.core

import scala.reflect.ClassTag

private[piplline]
trait MessageDriven {

  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message): Unit = queue.offer(mes)

  def sendTo(target: Class[_ <: MessageHandler], entity: Any): Unit = queue.offer(Message(target, entity))

  def sendTo[TARGET <: MessageHandler](entity: Any)(implicit target: ClassTag[TARGET]): Unit = queue.offer(Message(target.runtimeClass.asInstanceOf[Class[MessageHandler]], entity))

}
