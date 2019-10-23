package com.goexp.piplline.core

trait MessageHandler extends MessageDriven {
  def process(message: Message): Unit
}