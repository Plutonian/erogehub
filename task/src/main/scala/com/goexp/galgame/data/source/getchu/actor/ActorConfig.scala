package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.ansyn.Pool
import com.goexp.galgame.data.ansyn.Pool.{DB_POOL_SIZE, DOWN_POOL_SIZE}
import com.goexp.pipeline.handler.HandlerConfig

import java.util.concurrent.TimeUnit

object ActorConfig {

  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new ShutdownActor(2, TimeUnit.MINUTES), 1),
    new HandlerConfig(new InsertOrUpdateGameActor, DB_POOL_SIZE),
    new HandlerConfig(new DownloadPageActor, DOWN_POOL_SIZE),
    HandlerConfig(new ParsePageActor, Pool.CPU_POOL_SERV),
    new HandlerConfig(new SaveGameActor, DB_POOL_SIZE),
    HandlerConfig(new PrepareDownloadImageActor, Pool.CPU_POOL_SERV),
    new HandlerConfig(new DownloadImageActor, DOWN_POOL_SIZE),
    new HandlerConfig(new SaveHTMLActor, DOWN_POOL_SIZE)
  )
}
