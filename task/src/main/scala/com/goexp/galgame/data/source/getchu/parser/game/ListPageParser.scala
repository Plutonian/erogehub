package com.goexp.galgame.data.source.getchu.parser.game

import com.goexp.galgame.data.model.Game
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

/*
get game{
id,
smallImg
}

 */
class ListPageParser {


  def parse(item: Element): Game = {
    def parseId(url: String) = {
      """id=(\d+)""".r("id").findFirstMatchIn(url).map(_.group("id").toInt).getOrElse(0)
    }


    val g = new Game
    g.smallImg = item.select("img.lazy").attr("data-original")

    val itemContentEle = item.select("div.content_block")

    val url = itemContentEle.select("a.blueb").attr("href")
    g.id = parseId(url)

    val gType = itemContentEle.select("p span.orangeb").text
    g.isAdult = gType == "[PCゲーム・アダルト]"
    g

  }


  def parse(html: String): LazyList[Game] =
    Jsoup.parse(html)
      .select("ul.display>li>div.content_block")
      .asScala.to(LazyList)
      .map(parse)
}