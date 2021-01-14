package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.ansyn.Pool
import com.goexp.piplline.handler.HandlerConfig

import java.util.concurrent.TimeUnit

object ActorConfig {

  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new ShutdownActor(2, TimeUnit.MINUTES), 1),
    HandlerConfig(new ParsePageActor, Pool.CPU_POOL_SERV),
    HandlerConfig(new PreProcessGameActor, Pool.DB_POOL_SERV),
    HandlerConfig(new SaveGameActor, Pool.DB_POOL_SERV),
    HandlerConfig(new DownloadPageActor, Pool.DOWN_POOL_SERV),
    HandlerConfig(new DownloadImageActor, Pool.DOWN_POOL_SERV),
    HandlerConfig(new CheckStateActor, Pool.CPU_POOL_SERV)
  )
}
