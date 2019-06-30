package com.goexp.galgame.data.parser.game

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}

import com.goexp.galgame.data.model.Game
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

private object ListPageParser {
  private lazy val DATE_REGEX = "発売日：(?<date>\\d{4}/[0-1]\\d/[0-3]\\d)".r
}

class ListPageParser {


  def parse(item: Element): Game = {
    def parseId(url: String) = {
      "id=(?<id>\\d+)".r.findFirstMatchIn(url).map(_.group("id").toInt).getOrElse(0)
    }

    def parseDate(str: String) = {
      import ListPageParser._

      DATE_REGEX.findFirstMatchIn(str)
        .map(m => try LocalDate.parse(m.group("date"), DateTimeFormatter.ofPattern("yyyy/MM/dd")) catch {
          case e: DateTimeParseException => null
        })
        .orNull
    }

    val g = new Game
    g.smallImg = item.select("img.lazy").attr("data-original")
    val gEle = item.select("div.content_block a.blueb")
    g.name = gEle.text
    val url = gEle.attr("href")
    g.id = parseId(url)
    val raw = item.select("div.content_block p").text
    g.publishDate = parseDate(raw)
    g

  }


  def parse(html: String): LazyList[Game] =
    Jsoup.parse(html)
      .select("ul.display>li>div.content_block")
      .asScala.to(LazyList)
      .map(parse)
}