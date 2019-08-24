package com.goexp.common.util.web.url

import java.net.URI
import java.util.Objects

object URLUtil {

  def favUrl(url: String): String = {
    Objects.requireNonNull(url)
    val uri = URI.create(url)
    String.format("%s://%s/%s", uri.getScheme, uri.getHost, "favicon.ico")
  }
}
