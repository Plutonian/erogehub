package com.goexp.galgame.common.website

import com.goexp.common.util.web.url.UrlBuilder

object WikiURL {
  private val searchUrl = "https://ja.wikipedia.org/w/index.php"

  def fromTitle(title: String): String =
    UrlBuilder(searchUrl)
      .param("search", title)
      .build
}