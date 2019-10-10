package com.goexp.common.util


object TimerUtil {

  def apply(): TimerUtil = new TimerUtil()
}

class TimerUtil {

  private var old = 0L

  def start() = {
    old = System.currentTimeMillis
    this
  }


  def stop(): Unit = {
    val nextNow = System.currentTimeMillis
    print(s"\nUse ${nextNow - old} ms\n")
  }

}