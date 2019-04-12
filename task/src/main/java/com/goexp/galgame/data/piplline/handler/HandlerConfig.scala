package com.goexp.galgame.data.piplline.handler

import java.util.concurrent.ExecutorService

import com.goexp.galgame.data.piplline.core.MessageHandler

case class HandlerConfig[T](mesType: Int, messageHandler: MessageHandler[T], executor: ExecutorService) {
}