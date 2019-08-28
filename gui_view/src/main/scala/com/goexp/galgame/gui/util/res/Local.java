package com.goexp.galgame.gui.util.res;

import com.goexp.galgame.gui.util.cache.AppCache;
import com.goexp.galgame.gui.util.cache.Cache;
import javafx.scene.image.Image;

public class Local {

    public static Image getLocal(String name) {
        return getLocal(name, AppCache.imageMemCache());
    }

    private static Image getLocal(String name, Cache<String, Image> imageMemCache) {
        return imageMemCache.get(name).orElseGet(() -> {
            final var image = new Image(Local.class.getResource(name).toExternalForm());

            imageMemCache.put(name, image);
            return image;

        });
    }
}
