package com.goexp.galgame.common.website;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public final class DlSiteURL {

    private static final String searchUrl = "http://www.dlsite.com/pro/fsr/=/language/jp/keyword";

    public static String fromTitle(String title) {
        return String.format("%s/%s", searchUrl, URLEncoder.encode(title, Charset.forName("shift-jis")));
    }
}
