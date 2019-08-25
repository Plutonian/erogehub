package com.goexp.common.util


object TimerUtil {
  private var old = 0L

  def start(): Unit =
    old = System.currentTimeMillis

  def stop(): Unit = {
    val nextNow = System.currentTimeMillis
    print(s"\nUse ${nextNow - old} ms\n")
  }
}