package com.goexp.galgame.common.website

import com.goexp.common.util.web.url.URLSupport

object DlSiteURL {
  private val searchUrl = "http://www.dlsite.com/pro/fsr/=/language/jp/keyword"

  def fromTitle(title: String): String =
    s"$searchUrl/${title.urlEncode("shift-jis")}"
}