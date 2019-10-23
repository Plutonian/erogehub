package com.goexp.piplline.handler

import java.util.concurrent.{ExecutorService, Executors}

import com.goexp.piplline.core.MessageHandler

case class HandlerConfig(handler: MessageHandler, executor: ExecutorService) {

  def this(handler: MessageHandler, threadCount: Int) =
    this(handler, Executors.newFixedThreadPool(threadCount))

}