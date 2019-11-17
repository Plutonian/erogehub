package com.goexp.common.util.web

import java.net.ProxySelector
import java.net.http.HttpClient
import java.time.Duration
import java.util.concurrent.Executors


object HttpUtil {
  val httpClient = {
    HttpClient.newBuilder
      .followRedirects(HttpClient.Redirect.ALWAYS)
      .executor(Executors.newCachedThreadPool(r => {
        val t = new Thread(r)
        t.setDaemon(true)
        t
      }))
      .proxy(ProxySelector.getDefault)
      .connectTimeout(Duration.ofMinutes(2))
      .build
  }
  val noneProxyHttpClient = HttpClient.newBuilder
    .executor(Executors.newFixedThreadPool(100, (r: Runnable) => {
      val t = new Thread(r)
      t.setDaemon(true)
      t
    }))
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .connectTimeout(Duration.ofSeconds(60))
    .build

}