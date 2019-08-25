package com.goexp.galgame.common.website.getchu

import java.time.LocalDate

import com.goexp.common.util.web.url._
import com.goexp.common.util.date._

object GameList {
  def byDateRange(from: LocalDate, to: LocalDate): String =

    UrlBuilder(searchUrl)
      .param("search", 1)
      .param("sort", "release_date")
      .param("genre", "pc_soft")
      .param("start_date", from.format("yyyy/MM/dd"))
      .param("end_date", to.format("yyyy/MM/dd"))
      .param("list_count", 1000)
      .build

  def byBrand(brandId: Int): String =
    UrlBuilder(searchUrl)
      .param("search", 1)
      .param("sort", "release_date")
      .param("genre", "pc_soft")
      .param("search_brand_id", brandId)
      .param("list_count", 1000)
      .build

  private val searchUrl = "http://www.getchu.com/php/search.phtml"

  def getURLfromTitle(title: String): String =
    UrlBuilder(searchUrl)
      .param("sort", "release_date")
      .param("genre", "pc_soft")
      .param("search_keyword", title.urlEncode("shift-jis"))
      .build
}
