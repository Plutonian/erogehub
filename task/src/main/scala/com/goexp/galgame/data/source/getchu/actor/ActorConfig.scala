package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.ansyn.Pool
import com.goexp.piplline.handler.HandlerConfig

object ActorConfig {

  val DefaultGameProcessGroup = Set(
    HandlerConfig(new ParsePageActor, Pool.CPU_POOL_SERV),
    HandlerConfig(new PreProcessGameActor, Pool.DB_POOL_SERV),
    HandlerConfig(new SaveGameActor, Pool.DB_POOL_SERV),
    HandlerConfig(new DownloadPageActor, Pool.DOWN_POOL_SERV),
    HandlerConfig(new DownloadImageActor, Pool.DOWN_POOL_SERV),
    HandlerConfig(new CheckStateActor, Pool.CPU_POOL_SERV)
  )
}
