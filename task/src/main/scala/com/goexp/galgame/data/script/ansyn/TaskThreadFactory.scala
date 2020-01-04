package com.goexp.galgame.data.script.ansyn

import java.util.concurrent.ThreadFactory

class TaskThreadFactory extends ThreadFactory {
  override def newThread(r: Runnable): Thread = {
    val thread = new Thread(r)
    thread.setDaemon(true)
    thread
  }
}
