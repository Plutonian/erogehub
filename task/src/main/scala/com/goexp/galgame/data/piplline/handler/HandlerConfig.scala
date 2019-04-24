package com.goexp.galgame.data.piplline.handler

import java.util.concurrent.ExecutorService

import com.goexp.galgame.data.piplline.core.MessageHandler

case class HandlerConfig[T](mesCode: Int, handler: MessageHandler[T], executor: ExecutorService) {
}