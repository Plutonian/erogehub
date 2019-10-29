package com.goexp.galgame.common.website

import com.goexp.common.util.web.url._

object ErogameScapeURL {
  def byDateRange(year: Int): String =

    UrlBuilder(searchUrl)
      .param("erogame", "t")
      .param("text", "t")
      .param("coterie", "f")
      .param("year", year)
      .build

  //  def byBrand(brandId: Int): String =
  //    UrlBuilder(searchUrl)
  //      .param("search", 1)
  //      .param("sort", "release_date")
  //      .param("genre", "pc_soft")
  //      .param("search_brand_id", brandId)
  //      .param("list_count", 1000)
  //      .build

  private val searchUrl = "http://egs.omaera.org/~ap2/ero/toukei_kaiseki/toukei_hatubaibi.php"

  //  def getURLfromTitle(title: String): String =
  //    UrlBuilder(searchUrl)
  //      .param("sort", "release_date")
  //      .param("genre", "pc_soft")
  //      .param("search_keyword", title.urlEncode("shift-jis"))
  //      .build
}
