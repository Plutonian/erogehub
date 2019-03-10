package com.goexp.galgame.data;

import java.nio.file.Path;

public class Config {

    private final static Path CACHE_ROOT = com.goexp.galgame.common.Config.DATA_ROOT.resolve("cache");

    private final static Path ERROR_ROOT = com.goexp.galgame.common.Config.DATA_ROOT.resolve("error");

    public final static Path GAME_CACHE_ROOT = CACHE_ROOT.resolve("getchu/game/");

    public final static Path BRAND_CACHE_ROOT = CACHE_ROOT.resolve("getchu/brand/");

    public final static Path GAME_ERROR_FILE = ERROR_ROOT.resolve(Path.of("error_game.log"));

    public final static Path BRAND_ERROR_FILE = ERROR_ROOT.resolve(Path.of("error_brand.log"));
}
