package com.goexp.galgame.data.piplline.core

trait MessageHandler[In] {
  def process(message: Message[In]): Unit

  def setQueue(proxy: MessageQueueProxy[Message[_]]): Unit
}