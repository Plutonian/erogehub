package com.goexp.galgame.data.piplline.core

class Message[T](val code: Int, val entity: T) {
  override def toString: String = "Message{" + "code=" + code + '}'
}