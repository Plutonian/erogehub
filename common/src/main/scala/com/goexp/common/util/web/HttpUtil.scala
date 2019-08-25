package com.goexp.common.util.web

import java.net.ProxySelector
import java.net.http.HttpClient
import java.time.Duration

object HttpUtil {
  val httpClient: HttpClient = HttpClient.newBuilder
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .proxy(ProxySelector.getDefault)
    .connectTimeout(Duration.ofSeconds(60))
    .build
  val noneProxyHttpClient = HttpClient.newBuilder
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .proxy(ProxySelector.getDefault)
    .connectTimeout(Duration.ofSeconds(60))
    .build

}