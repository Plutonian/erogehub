package com.goexp.galgame.common.website.getchu

import com.goexp.common.util.date._
import com.goexp.common.util.web.url._

import java.time.LocalDate

object GameList {
  def byDateRange(from: LocalDate, to: LocalDate): String =

    UrlBuilder(searchUrl)
      .param("search", 1)
      .param("sort", "release_date")
      .param("genre", "pc_soft")
      .param("age", "18:lady")
      .param("start_date", from.format("yyyy/MM/dd"))
      .param("end_date", to.format("yyyy/MM/dd"))
      .param("list_count", 1000)
      .build

  def byBrand(brandId: Int): String =
    UrlBuilder(searchUrl)
      .param("search", 1)
      .param("sort", "release_date")
      .param("genre", "pc_soft")
      .param("age", "18:lady")
      .param("search_brand_id", brandId)
      .param("list_count", 1000)
      .build

  def byBrandDoujin(brandId: Int): String =
    UrlBuilder(searchUrl)
      .param("search", 1)
      .param("sort", "release_date")
      .param("genre", "doujin")
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
