package com.goexp.galgame.data.source.getchu.task

import com.goexp.galgame.data.script.ansyn.Pool
import com.goexp.piplline.handler.HandlerConfig

package object handler {

  val DefaultGameProcessGroup = Set(
    HandlerConfig(new ParsePage, Pool.CPU_POOL_SERV),
    HandlerConfig(new PreProcessGame, Pool.CPU_POOL_SERV),
    HandlerConfig(new Game2DB, Pool.DB_POOL_SERV),
    HandlerConfig(new DownloadPage, Pool.DOWN_POOL_SERV),
    HandlerConfig(new DownloadImage, Pool.DOWN_POOL_SERV),
    HandlerConfig(new CheckState, Pool.CPU_POOL_SERV)
  )
}
