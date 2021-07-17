package com.goexp.galgame.common.website

import com.goexp.common.util.web.url.UrlBuilder

object _2DFURL {
  private val searchUrl = "https://galge.fun/subjects/search"

  def fromTitle(title: String): String =
    UrlBuilder(searchUrl)
      .param("keyword", title)
      .build
}