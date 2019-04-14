package com.goexp.galgame.data

import java.nio.file.Path

import com.goexp.galgame.common

object Config {
  private lazy val CACHE_ROOT = common.Config.DATA_ROOT.resolve("cache")
  lazy val GAME_CACHE_ROOT: Path = CACHE_ROOT.resolve("getchu/game/")
  lazy val BRAND_CACHE_ROOT: Path = CACHE_ROOT.resolve("getchu/brand/")
}