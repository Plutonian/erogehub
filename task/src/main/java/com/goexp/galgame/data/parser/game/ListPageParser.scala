package com.goexp.galgame.data.parser.game

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.game.ListPageParser.GameParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._


object ListPageParser {

  object GameParser {

    def parse(item: Element) = {
      val g = new Game
      val img = item.select("img.lazy").attr("data-original")
      val gEle = item.select("div.content_block a.blueb")
      val name = gEle.text
      val url = gEle.attr("href")
      g.id = parseId(url)
      g.name = name
      g.smallImg = img
      val raw = item.select("div.content_block p").text
      g.publishDate = parseDate(raw)
      g
    }

    private def parseId(url: String) = {
      "id=(?<id>\\d+)".r.findFirstMatchIn(url).map(_.group("id").toInt).getOrElse(0)
    }

    private def parseDate(str: String) = {
      "発売日：(?<date>\\d{4}/[0-1]\\d/[0-3]\\d)".r.findFirstMatchIn(str)
        .map(m => LocalDate.parse(m.group("date"), DateTimeFormatter.ofPattern("yyyy/MM/dd")))
        .orNull
    }
  }

}

class ListPageParser {

  def parse(html: String): util.List[Game] =
    Jsoup.parse(html)
      .select("ul.display>li>div.content_block")
      .asScala.toStream
      .map(GameParser.parse)
      .asJava
}