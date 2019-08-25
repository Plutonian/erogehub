package com.goexp.galgame.common.website

import java.net.http.HttpRequest
import java.time.LocalDate

import com.goexp.common.util.date.formatDate
import com.goexp.common.util.web.url.{URLSupport, UrlBuilder}

object GetchuURL {


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

  object Game {
    def byId(gameId: Int): String =
      UrlBuilder("http://www.getchu.com/soft.phtml")
        .param("id", gameId)
        .build //                .param("gc", "gc")

    def SmallImg(gameId: Int): String =
      s"http://www.getchu.com/brandnew/$gameId/rc${gameId}package.jpg"

    def LargeImg(gameId: Int): String =
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

    //        public static String charSmall(int gameId, int charIndex) {
    //            return String.format("http://www.getchu.com/brandnew/%d/c%dchara%d.jpg", gameId, gameId, charIndex);
    //        }
  }

  object RequestBuilder {
    def create(url: String) = new RequestBuilder(url)

    def apply(url: String): RequestBuilder = new RequestBuilder(url)
  }

  class RequestBuilder private(val url: String) {


    private var builder = HttpRequest.newBuilder.uri(url.toURI())

    def adaltFlag: RequestBuilder = {
      builder = builder.header("Cookie", "getchu_adalt_flag=getchu.com")
      this
    }

    private def webClientParam = {
      builder = builder
        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .header("Accept-Encoding", "gzip, deflate")
        .header("Accept-Language", "ja,en-US;q=0.7,en;q=0.3")
        .header("Cache-Control", "max-age=0")
        .header("Upgrade-Insecure-Requests", "1")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
      this
    }

    def build: HttpRequest = {
      webClientParam
      builder.GET.build
    }
  }

}