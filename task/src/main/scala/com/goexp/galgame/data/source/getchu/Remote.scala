package com.goexp.galgame.data.source.getchu

import java.time.LocalDate

import com.goexp.common.util.Gzip._
import com.goexp.common.util.charset._
import com.goexp.galgame.common.website.getchu.{GameList, RequestBuilder}
import com.goexp.galgame.data.model.{Brand, Game}
import Client._
import com.goexp.galgame.data.source.getchu.parser.GetchuBrandParser
import com.goexp.galgame.data.source.getchu.parser.game.ListPageParser
import com.typesafe.scalalogging.Logger
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET

object GameRemote {
  private val logger = Logger(GameRemote.getClass)

  def from(brandId: Int): LazyList[Game] = {

    val request = RequestBuilder(GameList.byBrand(brandId)).adaltFlag.build
    val html = getHtml(request)

    new ListPageParser().parse(html)
  }

  def from(from: LocalDate, to: LocalDate): LazyList[Game] = {
    val url = GameList.byDateRange(from, to)
    val request = RequestBuilder(url).adaltFlag.build

    logger.debug(url)

    val html = getHtml(request)

    new ListPageParser().parse(html)
  }

  //from local
  def from(bytes: Array[Byte]): LazyList[Game] = {
    val html = bytes.unGzip().decode(DEFAULT_CHARSET)
    new ListPageParser().parse(html)
  }
}

object BrandService {
  private val logger = Logger(GameRemote.getClass)


  def all(): LazyList[Brand] = {

    val request = RequestBuilder("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
    val html = getHtml(request)

    logger.debug(html)

    new GetchuBrandParser().parse(html)

  }
}