package com.goexp.galgame.common.website.getchu

import com.goexp.common.util.web.url.UrlBuilder

object GetchuGameRemote {
  def byId(gameId: Int): String =
    UrlBuilder("http://www.getchu.com/soft.phtml")
      .param("id", gameId)
      .build //                .param("gc", "gc")

  def smallImg(gameId: Int): String =
    s"http://www.getchu.com/brandnew/$gameId/rc${gameId}package.jpg"

  def tiny200Img(gameId: Int): String =
    s"http://www.getchu.com/brandnew/$gameId/c${gameId}package_s.jpg"

  def largeImg(gameId: Int): String =
    s"http://www.getchu.com/brandnew/$gameId/c${gameId}package.jpg"

  def largeSimpleImg(src: String): String =
    getUrlFromSrc(src)

  def smallSimpleImg(src: String): String = {
    val ts = src.replace(".jpg", "_s.jpg")
    getUrlFromSrc(ts)
  }

  def getUrlFromSrc(src: String): String = {
    if (src.startsWith("http") || src.startsWith("https")) return src

    s"http://www.getchu.com${src.replaceFirst("\\.", "")}"
  }

}
