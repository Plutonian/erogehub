package com.goexp.galgame.common.website

import com.goexp.common.util.UrlBuilder

object _2DFURL {
  private val searchUrl = "https://www.2dfan.com/subjects/search"

  def fromTitle(title: String): String = UrlBuilder.create(searchUrl).param("keyword", title).build
}