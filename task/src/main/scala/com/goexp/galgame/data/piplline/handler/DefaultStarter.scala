package com.goexp.galgame.data.piplline.handler

import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}

abstract class DefaultStarter[T] extends DefaultMessageHandler[T] with Starter {
  override def process(message: Message[T], msgQueue: MessageQueueProxy[Message[_]]) = process(msgQueue)
}