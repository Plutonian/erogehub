package com.goexp.piplline.core

trait Starter extends MessageDriven {
  def process(): Unit

}