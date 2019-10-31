package com.goexp.piplline.handler

import com.goexp.piplline.core.{Message, MessageHandler}

abstract class DefaultHandler extends MessageHandler with EntityHandler {
  override def process(message: Message): Unit = {
    handle(message)
  }
}
