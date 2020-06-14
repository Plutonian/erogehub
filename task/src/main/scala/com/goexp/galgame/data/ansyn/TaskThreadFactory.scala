package com.goexp.galgame.data.ansyn

import java.util.concurrent.ThreadFactory

class TaskThreadFactory(name: String) extends ThreadFactory {
  var i = 0

  override def newThread(r: Runnable): Thread = {
    val thread = new Thread(r)
    thread.setName(s"${name}-${i + 1}")
    thread.setDaemon(true)
    i += 1
    thread
  }
}
