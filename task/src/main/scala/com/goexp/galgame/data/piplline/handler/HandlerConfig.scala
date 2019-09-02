package com.goexp.galgame.data.piplline.handler

import java.util.concurrent.{ExecutorService, Executors}

import com.goexp.galgame.data.piplline.core.MessageHandler

case class HandlerConfig(handler: MessageHandler, executor: ExecutorService) {

  def this(handler: MessageHandler, threadCount: Int) =
    this(handler, Executors.newFixedThreadPool(threadCount))

}