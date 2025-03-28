package com.goexp.galgame.common.util

import java.net.ProxySelector
import java.net.http.HttpClient
import java.time.Duration
import java.util.concurrent.Executors
import javax.net.ssl.SSLContext

object SpClient {
  //   创建SSLContext
  private val sslContext = SSLContext.getInstance("TLS");

  //   初始化
  sslContext.init(null, Array(new MyX509TrustManager()), new java.security.SecureRandom());


  val httpClient: HttpClient =
    HttpClient.newBuilder
      .followRedirects(HttpClient.Redirect.ALWAYS)
      .executor(Executors.newCachedThreadPool(r => {
        val t = new Thread(r)
        t.setDaemon(true)
        t
      }))
      .sslContext(sslContext)
      .proxy(ProxySelector.getDefault)
      .connectTimeout(Duration.ofMinutes(2))
      .build


}
