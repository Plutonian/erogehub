package com.goexp.galgame.data.task.local.cal

import java.util.concurrent.ThreadFactory

class CalThreadFactory extends ThreadFactory {
  override def newThread(r: Runnable): Thread = {
    val thread = new Thread(r)
    thread.setDaemon(true)
    thread
  }
}
