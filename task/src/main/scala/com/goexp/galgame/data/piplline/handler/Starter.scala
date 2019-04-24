package com.goexp.galgame.data.piplline.handler

import com.goexp.galgame.data.piplline.core.Message
import com.goexp.galgame.data.piplline.core.MessageQueueProxy
import java.util.concurrent.BlockingQueue

trait Starter {
  def process(): Unit

  def setQueue(proxy: MessageQueueProxy[Message[_]]): Unit
}