package com.goexp.galgame.data.ansyn

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Pool {
  val DOWN_POOL_SIZE = 30
  val DB_POOL_SIZE = 10

  val DOWN_POOL_SERV = Executors.newFixedThreadPool(30, new TaskThreadFactory("download"))
  val DB_POOL_SERV = Executors.newFixedThreadPool(10, new TaskThreadFactory("db"))
  val CPU_POOL_SERV = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors(), new TaskThreadFactory("cpu"))


  val DB_POOL = ExecutionContext.fromExecutor(DB_POOL_SERV)
  val DOWN_POOL = ExecutionContext.fromExecutor(DOWN_POOL_SERV)
  implicit val CPU_POOL: ExecutionContextExecutor = ExecutionContext.fromExecutor(CPU_POOL_SERV)

}
