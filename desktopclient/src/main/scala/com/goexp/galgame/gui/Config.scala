package com.goexp.galgame.gui

import com.goexp.galgame.common.Config.{DATA_ROOT, prop}

object Config {
  val IMG_PATH = DATA_ROOT.resolve("img/game/")
  val DOWNLOAD_REMOTE_IMG=prop.getString("DOWNLOAD_REMOTE_IMG").toBoolean
}