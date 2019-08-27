package com.goexp.galgame.data.piplline.core

case class Message[T](code: Int, entity: T) {
  override def toString: String = "Message{" + "code=" + code + '}'
}