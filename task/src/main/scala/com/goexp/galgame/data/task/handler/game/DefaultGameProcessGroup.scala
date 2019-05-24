package com.goexp.galgame.data.task.handler.game

import com.goexp.galgame.data.piplline.handler.{HandlerConfig, HandlerConfigGroup}
import com.goexp.galgame.data.task.handler.MesType

object DefaultGameProcessGroup extends HandlerConfigGroup(
  new HandlerConfig(MesType.Game, new LocalGameHandler, 2),
  new HandlerConfig(MesType.ContentBytes, new Bytes2Html, 30),
  new HandlerConfig(MesType.ContentHtml, new Html2GameOK, 30),
  new HandlerConfig(MesType.GAME_OK, new ProcessGameOK, 30)) {
}
