package com.goexp.galgame.data.piplline.core

case class Message(code: Int, entity: Any) {
  override def toString: String = "Message{" + "code=" + code + '}'
}