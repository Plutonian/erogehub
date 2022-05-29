package com.goexp.galgame.data.source.getchu.parser.game

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

  case class ListItem(id: Int = 0,
//                      isAdult: Boolean = false,
                      smallImg: String = "")


  def parse(item: Element): ListItem = {
    def parseId(url: String) = {
      """id=(?<id>\d+)""".r.findFirstMatchIn(url).map(_.group("id").toInt).getOrElse(0)
    }

    //    val g = new Game
    val smallImg = item.select("img.lazy").attr("data-original")
    val itemContentEle = item.select("div.content_block")

    val url = itemContentEle.select("a.blueb").attr("href")
    val id = parseId(url)

//    val gType = itemContentEle.select("p span.orangeb").text
//    val isAdult = gType == "[PCゲーム・アダルト]"

    ListItem(
      id,
//      isAdult,
      smallImg
    )

  }


  def parse(html: String): LazyList[ListItem] =
    Jsoup.parse(html)
      .select("ul.display>li>div.content_block")
      .asScala.to(LazyList)
      .map(parse)
}