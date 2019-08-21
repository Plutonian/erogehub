package com.goexp.common.util;

import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;

public class WebUtil {

    public static HttpClient httpClient = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .proxy(ProxySelector.getDefault())
            .connectTimeout(Duration.ofSeconds(60))
            .build();

    private static HttpClient noneProxyHttpClient;

    public static HttpClient noneProxyClient() {

        if (noneProxyHttpClient == null) {
            noneProxyHttpClient = HttpClient
                    .newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .proxy(ProxySelector.getDefault())
                    .connectTimeout(Duration.ofSeconds(60))
                    .build();
        }

        return noneProxyHttpClient;
    }


}
