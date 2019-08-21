package com.goexp.galgame.common.website

import com.goexp.common.util.UrlBuilder

object GGBasesURL {
  private val searchUrl = "https://www.ggbases.com/search.so"

  def fromTitle(title: String): String = UrlBuilder.create(searchUrl).param("p", "0").param("title", title).param("advanced", "").build
}