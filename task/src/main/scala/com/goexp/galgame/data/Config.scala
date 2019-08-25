package com.goexp.galgame.data

import com.goexp.galgame.common.Config.DATA_ROOT

object Config {
  private val CACHE_ROOT = DATA_ROOT.resolve("cache")
  val GAME_CACHE_ROOT = CACHE_ROOT.resolve("getchu/game/")
  val BRAND_CACHE_ROOT = CACHE_ROOT.resolve("getchu/brand/")
}