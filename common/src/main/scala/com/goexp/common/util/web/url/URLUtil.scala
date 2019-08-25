package com.goexp.common.util.web.url

import java.util.Objects

object URLUtil {

  def favUrl(url: String): String = {
    Objects.requireNonNull(url)
    val uri = url.toURI()
    s"${uri.getScheme}://${uri.getHost}/favicon.ico"
  }
}
