package com.goexp.galgame.data

import java.nio.file.Path

object Config {
  private val CACHE_ROOT = com.goexp.galgame.common.Config.DATA_ROOT.resolve("cache")
  val GAME_CACHE_ROOT: Path = CACHE_ROOT.resolve("getchu/game/")
  val BRAND_CACHE_ROOT: Path = CACHE_ROOT.resolve("getchu/brand/")
}