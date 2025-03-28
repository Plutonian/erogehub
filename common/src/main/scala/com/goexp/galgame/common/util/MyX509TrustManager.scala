package com.goexp.galgame.common.util

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class MyX509TrustManager extends X509TrustManager {
  override def checkClientTrusted(chain: Array[X509Certificate], authType: String): Unit = {

  }

  override def checkServerTrusted(chain: Array[X509Certificate], authType: String): Unit = {

  }

  override def getAcceptedIssuers: Array[X509Certificate] = {
    null
  }
}
