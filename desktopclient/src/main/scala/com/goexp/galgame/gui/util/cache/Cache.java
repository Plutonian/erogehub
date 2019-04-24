package com.goexp.galgame.gui.util.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Optional;

public class Cache<K, V> {

    private final HashMap<K, SoftReference<V>> imageCache;

    public Cache() {
        this.imageCache = new HashMap<>();
    }

    public Optional<V> get(K key) {

        return Optional.ofNullable(imageCache.get(key)).map(cache -> cache.get());
//        var img = imageMemCache.get(key);
//        if (img != null && img.get() != null) {
//            return img.get();
//        }
//        return null;
    }

    public SoftReference<V> put(K key, V value) {
        return imageCache.put(key, new SoftReference<>(value));
    }

    public SoftReference<V> remove(K key) {
        return imageCache.remove(key);
    }
}
