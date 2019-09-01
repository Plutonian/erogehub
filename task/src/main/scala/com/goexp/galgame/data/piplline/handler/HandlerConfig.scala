package com.goexp.galgame.data.piplline.handler

import java.util.concurrent.{ExecutorService, Executors}

import com.goexp.galgame.data.piplline.core.{Message, MessageHandler}

case class HandlerConfig(mesCode: Int, handler: MessageHandler, executor: ExecutorService) {

  def this(mesCode: Int, handler: MessageHandler, threadCount: Int) =
    this(mesCode, handler, Executors.newFixedThreadPool(threadCount))

}