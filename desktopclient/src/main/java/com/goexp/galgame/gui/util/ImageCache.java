package com.goexp.galgame.gui.util;

import javafx.scene.image.Image;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class ImageCache {

    private static HashMap<String, SoftReference<Image>> imageCache = new HashMap<>();


    Image get(String key) {

        var img = imageCache.get(key);
        if (img != null && img.get() != null) {
            return img.get();
        }
        return null;
    }

    SoftReference<Image> put(String key, Image value) {
        return imageCache.put(key, new SoftReference<>(value));
    }

    SoftReference<Image> remove(String key) {
        return imageCache.remove(key);
    }
}
