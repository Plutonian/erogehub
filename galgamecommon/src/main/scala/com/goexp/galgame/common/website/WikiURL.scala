package com.goexp.galgame.common.website

import com.goexp.common.util.UrlBuilder

object WikiURL {
  private val searchUrl = "https://ja.wikipedia.org/w/index.php"

  def fromTitle(title: String): String = UrlBuilder.create(searchUrl).param("search", title).build
}