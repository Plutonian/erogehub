package com.goexp.galgame.data.task.local

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

package object cal {

  val ioPool = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30, new CalThreadFactory))
  val cpuPool = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors(), new CalThreadFactory))

}
