package com.goexp.galgame.common.website

import com.goexp.common.util.web.url._

object ErogameScapeURL {

  def byDateRange(year: Int): String = {
    UrlBuilder("https://erogamescape.dyndns.org/~ap2/ero/toukei_kaiseki/toukei_hatubaibi.php")
      .param("erogame", "t")
      .param("text", "t")
      .param("coterie", "f")
      .param("year", year)
      .build
  }

  def byIdBasic(id: Int): String = {
    UrlBuilder("https://erogamescape.dyndns.org/~ap2/ero/toukei_kaiseki/game.php")
      .param("game", id)
      .build
  }

  def fromTitle(title: String): String =
    UrlBuilder("https://erogamescape.dyndns.org/~ap2/ero/toukei_kaiseki/kensaku.php")
      .param("category", "game")
      .param("word_category", "name")
      .param("mode", "normal")
      .param("word", title)
      .build
}
