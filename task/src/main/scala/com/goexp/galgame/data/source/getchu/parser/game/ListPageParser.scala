package com.goexp.galgame.data.source.getchu.parser.game

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}

import com.goexp.galgame.data.model.Game
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._


private object ListPageParser {
  private val DATE_REGEX = """発売日：(\d{4}/[0-1]\d/[0-3]\d)""".r("date")
}

/*
get game{
id,
name,
publishDate,
smallImg
}

 */
class ListPageParser {


  def parse(item: Element): Game = {
    def parseId(url: String) = {
      """id=(\d+)""".r("id").findFirstMatchIn(url).map(_.group("id").toInt).getOrElse(0)
    }

    def parseDate(str: String) = {
      import ListPageParser._

      DATE_REGEX.findFirstMatchIn(str)
        .map { m =>
          try LocalDate.parse(m.group("date"), DateTimeFormatter.ofPattern("yyyy/MM/dd")) catch {
            case _: DateTimeParseException => null
          }
        }
        .orNull
    }

    val g = new Game
    g.smallImg = item.select("img.lazy").attr("data-original")

    val itemContentEle = item.select("div.content_block")

    val titleEle = itemContentEle.select("a.blueb")
    g.name = titleEle.text
    val url = titleEle.attr("href")
    g.id = parseId(url)

    val raw = itemContentEle.select("p").text
    g.publishDate = parseDate(raw)
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