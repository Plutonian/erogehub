package com.goexp.galgame.data;

import java.nio.file.Path;

public class Config {

    private final static Path CACHE_ROOT = com.goexp.galgame.common.Config.DATA_ROOT.resolve("cache");
    public final static Path GAME_CACHE_ROOT = CACHE_ROOT.resolve("getchu/game/");
    public final static Path BRAND_CACHE_ROOT = CACHE_ROOT.resolve("getchu/brand/");
}
