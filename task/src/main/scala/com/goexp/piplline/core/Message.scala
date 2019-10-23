package com.goexp.piplline.core

case class Message(code: Int, entity: Any) {
  override def toString: String = "Message{" + "code=" + code + '}'
}