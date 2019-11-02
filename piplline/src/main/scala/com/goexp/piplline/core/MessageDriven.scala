package com.goexp.piplline.core

import scala.beans.BeanProperty
import scala.reflect.ClassTag

private[piplline]
trait MessageDriven {

  @BeanProperty
  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message): Unit = queue.offer(mes)

  def sendTo(target: Class[_ <: MessageHandler], entity: Any): Unit = queue.offer(Message(target, entity))

  def sendTo[TO <: MessageHandler](entity: Any)(implicit ct: ClassTag[TO]): Unit = queue.offer(Message(ct.runtimeClass.asInstanceOf[Class[MessageHandler]], entity))

}
