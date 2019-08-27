package com.goexp.galgame.data.piplline.core

trait MessageHandler[In] extends MessageDriven {
  def process(message: Message[In]): Unit
}