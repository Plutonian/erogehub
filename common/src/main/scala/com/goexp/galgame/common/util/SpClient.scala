package com.goexp.galgame.common.util

import java.net.{CookieHandler, CookieManager, HttpCookie, ProxySelector, URI}
import java.net.http.HttpClient
import java.time.Duration
import java.util.concurrent.Executors
import javax.net.ssl.SSLContext

object SpClient {
  //   创建SSLContext
  private val sslContext = SSLContext.getInstance("TLS");

  //   初始化
  sslContext.init(null, Array(new MyX509TrustManager()), new java.security.SecureRandom());


  val sessionCookie = new HttpCookie("getchu_adalt_flag", "getchu.com")
  sessionCookie.setPath("/")
  sessionCookie.setVersion(0)

  val cookieManager = new CookieManager()
  cookieManager.getCookieStore.add(new URI("http://www.getchu.com"), sessionCookie)

  val httpClient: HttpClient =
    HttpClient.newBuilder
      .cookieHandler(cookieManager)
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
