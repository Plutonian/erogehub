package com.goexp.galgame.data.task.handler

import com.goexp.galgame.data.piplline.handler.{HandlerConfig, HandlerConfigGroup}

package object game {
  val DefaultGameProcessGroup = new HandlerConfigGroup(
    new HandlerConfig(MesType.ContentBytes, new Bytes2Html, 2),
    new HandlerConfig(MesType.ContentHtml, new Html2GameOK, 2),
    new HandlerConfig(MesType.GAME_OK, new ProcessGameOK, 30)
  )
}
