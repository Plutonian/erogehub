package com.goexp.galgame.common.website;

import com.goexp.common.util.UrlBuilder;

public final class GGBasesURL {

    private static final String searchUrl = "https://www.ggbases.com/search.so";

    public static String fromTitle(String title) {
        return UrlBuilder
                .create(searchUrl)
                .param("p", "0")
                .param("title", title)
                .param("advanced", "")
                .build();
    }

}
