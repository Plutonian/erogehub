package com.goexp.galgame.data.source.getchu

import com.goexp.galgame.common.website.getchu.RequestBuilder
import com.goexp.galgame.data.model.Brand
import com.goexp.galgame.data.source.getchu.PageDownloader._
import com.goexp.galgame.data.source.getchu.parser.GetchuBrandParser
import com.typesafe.scalalogging.Logger
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET

object BrandService {
  private val logger = Logger(BrandService.getClass)


  def all(): LazyList[Brand] = {

    val request = RequestBuilder("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
    val html = download(request)

    logger.debug(html)

    new GetchuBrandParser().parse(html)

  }
}