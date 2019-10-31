package com.goexp.piplline.core

private[piplline]
trait MessageHandler extends MessageDriven {
  def process(message: Message): Unit
}