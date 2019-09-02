package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.handler.HandlerConfig

package object game {
  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new Bytes2Html, 2),
    new HandlerConfig(new Html2GameOK, 2),
    new HandlerConfig(new ProcessGameOK, 30)
  )
}
