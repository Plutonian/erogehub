package com.goexp.galgame.data.source.getchu.task

import com.goexp.piplline.handler.HandlerConfig

package object handler {
  val DefaultGameProcessGroup = Set(
    new HandlerConfig(new ParsePage, 2),
    new HandlerConfig(new PreProcessGame, 2),
    new HandlerConfig(new Game2DB, 2),
    new HandlerConfig(new DownloadPage, 30),
    new HandlerConfig(new DownloadImage, 30)
  )
}
