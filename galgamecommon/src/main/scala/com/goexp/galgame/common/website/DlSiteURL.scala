package com.goexp.galgame.common.website

import java.net.URLEncoder
import java.nio.charset.Charset

object DlSiteURL {
  private val searchUrl = "http://www.dlsite.com/pro/fsr/=/language/jp/keyword"

  def fromTitle(title: String): String = String.format("%s/%s", searchUrl, URLEncoder.encode(title, Charset.forName("shift-jis")))
}