package com.goexp.galgame.gui.util.res.gameimg;

public class CacheKey {
    private final String diskCacheKey;
    private final String memCacheKey;

    CacheKey(String diskCacheKey, String memCacheKey) {
        this.diskCacheKey = diskCacheKey;
        this.memCacheKey = memCacheKey;
    }

    public String getDiskCacheKey() {
        return diskCacheKey;
    }

    public String getMemCacheKey() {
        return memCacheKey;
    }
}
