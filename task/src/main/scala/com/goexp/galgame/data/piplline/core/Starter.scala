package com.goexp.galgame.data.piplline.core

trait Starter extends MessageDriven {
  def process(): Unit

}