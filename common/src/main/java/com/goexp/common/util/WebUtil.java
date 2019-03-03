package com.goexp.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.zip.GZIPInputStream;

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


    public static String decodeGzip(byte[] gzipBytes, Charset charset) {
        return charset.decode(ByteBuffer.wrap(decodeGzip(gzipBytes))).toString();
    }

    public static byte[] decodeGzip(byte[] gzipBytes) {
        try (var s = new GZIPInputStream(new ByteArrayInputStream(gzipBytes))) {
            return s.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
