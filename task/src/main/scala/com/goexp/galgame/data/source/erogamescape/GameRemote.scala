package com.goexp.galgame.data.source.erogamescape

import com.goexp.galgame.common.website.ErogameScapeURL
import com.goexp.galgame.common.website.getchu.RequestBuilder
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.Client.{DEFAULT_CHARSET, getHtml}
import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser
import org.slf4j.LoggerFactory

object GameRemote {
  private val logger = LoggerFactory.getLogger(GameRemote.getClass)

  def from(year: Int): LazyList[Game] = {
    val url = ErogameScapeURL.byDateRange(year)
    val request = RequestBuilder(url).build

    logger.debug(url)

    val html = getHtml(request)

    new ListPageParser().parse(html)
  }

  def main(args: Array[String]): Unit = {
    GameRemote.from(2019)
      .foreach {
        g => println(g)
      }
  }
}
