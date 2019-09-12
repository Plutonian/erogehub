package com.goexp.galgame.data.task.ansyn

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object Pool {

  val IO_POOL = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30, new TaskThreadFactory))
  val CPU_POOL = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors(), new TaskThreadFactory))

}
