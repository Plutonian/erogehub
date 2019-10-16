package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.handler.HandlerConfig

package object game {
  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new Html2GameOK, 2),
    new HandlerConfig(new Game2DB, 30)
  )
}
