package com.goexp.piplline.core

case class Message(target: Int, entity: Any) {

  def this(clazz: Class[_], entity: Any) =
    this(clazz.hashCode(), entity)


  override def toString: String = "Message{" + "code=" + target + '}'
}