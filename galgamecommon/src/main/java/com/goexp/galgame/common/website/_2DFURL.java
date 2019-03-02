package com.goexp.galgame.common.website;

import com.goexp.common.util.UrlBuilder;

public final class _2DFURL {

    public static final String searchUrl = "https://www.2dfan.com/subjects/search";

    public static String fromTitle(String title) {

        return UrlBuilder
                .create(searchUrl)
                .param("keyword", title)
                .build();
    }

}
