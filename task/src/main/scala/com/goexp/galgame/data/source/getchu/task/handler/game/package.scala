package com.goexp.galgame.data.source.getchu.task.handler

import com.goexp.piplline.handler.HandlerConfig

package object game {
  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new Html2GameOK, 2),
    new HandlerConfig(new Game2DB, 2),
    new HandlerConfig(new DownloadImage, 30)
  )
}
