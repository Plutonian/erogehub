package com.goexp.galgame.common.website;

import com.goexp.common.util.UrlBuilder;

public final class WikiURL {

    public static final String searchUrl = "https://ja.wikipedia.org/w/index.php";

    public static String fromTitle(String title) {
        return UrlBuilder
                .create(searchUrl)
                .param("search", title)
                .build();
    }

}
