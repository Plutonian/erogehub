package com.goexp.galgame.data;

import java.nio.file.Path;

public class Config {

    public final static Path CACHE_ROOT = Path.of("C:\\Users\\K\\work\\galgame\\cache\\");

    public final static Path ERROR_ROOT = Path.of("C:\\Users\\K\\work\\galgame\\error\\");

    public final static Path GAME_CACHE_ROOT = CACHE_ROOT.resolve("getchu\\game\\");

    public final static Path BRAND_CACHE_ROOT = CACHE_ROOT.resolve("getchu\\brand\\");

    public final static Path GAME_ERROR_FILE = ERROR_ROOT.resolve(Path.of("error_game.log"));

    public final static Path BRAND_ERROR_FILE = ERROR_ROOT.resolve(Path.of("error_brand.log"));
}
