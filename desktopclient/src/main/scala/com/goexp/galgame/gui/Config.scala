package com.goexp.galgame.gui

import java.nio.file.Path
import com.goexp.galgame.common.Config.DATA_ROOT

object Config {
  val IMG_PATH: Path = DATA_ROOT.resolve("img/game/")
}