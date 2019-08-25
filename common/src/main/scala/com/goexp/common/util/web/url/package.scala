package com.goexp.common.util.web

import java.net.{URI, URLEncoder}
import java.nio.charset.Charset

package object url {

  implicit class URLSupport(val url: String) {
    def toURI() = {
      URI.create(url)
    }

    def urlEncode(charset: String) = {
      URLEncoder.encode(url, charset)
    }

    def urlEncode(charset: Charset) = {
      URLEncoder.encode(url, charset)
    }
  }

}
