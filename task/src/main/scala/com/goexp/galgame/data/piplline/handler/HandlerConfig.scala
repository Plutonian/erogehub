package com.goexp.galgame.data.piplline.handler

import java.util.concurrent.{ExecutorService, Executors}

import com.goexp.galgame.data.piplline.core.MessageHandler

case class HandlerConfig[T](mesCode: Int, handler: MessageHandler[T], executor: ExecutorService) {

  def this(mesCode: Int, handler: MessageHandler[T], threadCount: Int) =
    this(mesCode, handler, Executors.newFixedThreadPool(threadCount))

}