package com.goexp.galgame.common.website;

public final class BangumiURL {

    public static final String searchUrl = "http://bgm.tv/subject_search";

    public static String fromTitle(String title) {
        return String.format("%s/%s", searchUrl, title);
    }

}
