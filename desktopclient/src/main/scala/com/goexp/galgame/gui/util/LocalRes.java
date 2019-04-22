package com.goexp.galgame.gui.util;

import javafx.scene.image.Image;

public final class LocalRes {
    public static final LocalRes BRAND_16_PNG = new LocalRes("/ico/16/brand_16.png");
    public static final LocalRes SEARCH_16_PNG = new LocalRes("/ico/16/search_16.png");
    public static final LocalRes DATE_16_PNG = new LocalRes("/ico/16/date_16.png");
    public static final LocalRes CV_16_PNG = new LocalRes("/ico/16/cv_16.png");
    public static final LocalRes GAME_16_PNG = new LocalRes("/ico/16/game_16.png");
    public static final LocalRes TAG_16_PNG = new LocalRes("/ico/16/tag_16.png");
    public static final LocalRes HEART_16_PNG = new LocalRes("/ico/16/heart_16.png");

    public static final LocalRes HEART_32_PNG = new LocalRes("/ico/32/heart_32.png");

    private static final String ROOT = "/ico";

    private final String path;

    private final String pattern;

    private LocalRes(String path) {
        this(path, "");
    }

    private LocalRes(String path, String pattern) {
        this.path = path;
        this.pattern = pattern;
    }

    public final Image get() {
        return Images.Local.getLocal(path);
    }


}
