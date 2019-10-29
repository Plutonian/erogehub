package com.goexp.galgame.common.website

import com.goexp.common.util.web.url._

object ErogameScapeURL {

  def byDateRange(year: Int): String = {
    UrlBuilder("http://egs.omaera.org/~ap2/ero/toukei_kaiseki/toukei_hatubaibi.php")
      .param("erogame", "t")
      .param("text", "t")
      .param("coterie", "f")
      .param("year", year)
      .build
  }

  def byIdBasic(id: Int): String = {
    UrlBuilder("http://egs.omaera.org/~ap2/ero/toukei_kaiseki/game.php")
      .param("game", id)
      .build
  }

  //  def getURLfromTitle(title: String): String =
  //    UrlBuilder(searchUrl)
  //      .param("sort", "release_date")
  //      .param("genre", "pc_soft")
  //      .param("search_keyword", title.urlEncode("shift-jis"))
  //      .build
}
