package com.goexp.common.util.web.url;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class UrlBuilder {

    private final String host;

    private final Map<String, String> params = new HashMap<>();


    private UrlBuilder(final String host) {
        this.host = host;
    }

    public static UrlBuilder create(final String host) {
        return new UrlBuilder(host);
    }

    public final UrlBuilder param(final String name, final String value) {
        params.put(name, value);

        return this;
    }

    public final String build() {

        var queryString = params
                .entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(),
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)
                ))
                .collect(Collectors.joining("&"));


        return String.format("%s?%s", host, queryString);
    }
}
