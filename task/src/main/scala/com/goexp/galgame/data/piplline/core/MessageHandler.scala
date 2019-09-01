package com.goexp.galgame.data.piplline.core

trait MessageHandler extends MessageDriven {
  def process(message: Message): Unit
}