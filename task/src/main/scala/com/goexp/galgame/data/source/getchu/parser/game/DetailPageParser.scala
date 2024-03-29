package com.goexp.galgame.data.source.getchu.parser.game

import com.goexp.galgame.common.model.game.{GameCharacter, GameImg}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.parser.game.DetailPageParser.{DetailParser, GameCharParser, SimpleImgParser}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import scala.jdk.CollectionConverters._

private object DetailPageParser {

  private object DetailParser {
    private val BRAND_ID_REGEX = """search_brand_id=(?<brandid>\d+)$""".r
  }

  private class DetailParser {

    import DetailParser._

    def parseDate(str: String) = {
      try LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy/MM/dd")) catch {
        case _: DateTimeParseException => null
      }
    }

    def parse(gameId: Int, root: Document) = {

      val ele = root.select("#soft_table >tbody>tr:nth-of-type(2)")
      val g = new Game
      g.id = gameId
      g.name = root.select("#soft-title").first().text().replace("（このタイトルの関連商品）", "").trim
      g.publishDate = parseDate(ele.select("td:contains(発売日)").next.text)
      g.painter = ele.select("td:contains(原画)").next.text.split("、").to(LazyList).map(s => s.trim).asJava
      g.writer = ele.select("td:contains(シナリオ)").next.text.split("、").to(LazyList).map(s => s.trim).asJava
      g.`type` = ele.select("td:contains(サブジャンル)").next.text.replace("[一覧]", "").split("、").to(LazyList).map(s => s.trim).asJava
      g.tag = ele.select("td:contains(カテゴリ)").next.text.replace("[一覧]", "").split("、").to(LazyList).map(s => s.trim).asJava
      g.story = root.select("#wrapper div.tabletitle:contains(ストーリー)").next.html.replaceAll("<[^>]*>", "").replace("<br>", "").replace(" ", "")
      g.intro = root.select("#wrapper div.tabletitle:contains(商品紹介)").next.html.replaceAll("<[^>]*>", "").replace("<br>", "").replace(" ", "")
      val brandUrl = ele.select("a:contains(このブランドの作品一覧)").attr("href")
      g.brandId = BRAND_ID_REGEX.findFirstMatchIn(brandUrl).map(m => m.group("brandid").toInt).getOrElse(0)
      g
    }
  }

  private object GameCharParser {
    private val CV_REGEX = """（?[Cc][vV]\s*[：:.／/]?\s*(?<cv>[^）]+)）?$""".r
  }

  private class GameCharParser {
    private var index = 1

    private def parseName(str: String) = str.replaceAll("（?[Cc][vV][：:.／/]?）?", "").trim

    private def parseCV(str: String) = {
      import GameCharParser._

      CV_REGEX.findFirstMatchIn(str).map(m => m.group("cv").replaceAll("""[\s　]""", "")).getOrElse("")
    }

    def parse(root: Document) =
      root.select("#wrapper div.tabletitle:contains(キャラクター)").next.select("tbody>tr:nth-of-type(2n+1)").asScala
        .to(LazyList)
        .map { tr =>
          val title = tr.select("h2.chara-name").text
          val cv = parseCV(title)

          val person = GameCharacter(
            name = parseName(title.replace(cv, "")),
            cv = cv,
            intro = tr.select("dl dd").html.replaceAll("<[^>]*>", "").trim,
            trueCV = "",
            img = tr.select("td:nth-of-type(1)>img").attr("src"),
            index = index
          )
          index += 1
          person
        }
  }

  private class SimpleImgParser() {
    private var imgIndex = 1

    def parse(root: Document) =
      root.select("#wrapper div.tabletitle:contains(サンプル画像)").next.select("a.highslide")
        .asScala.to(LazyList)
        .map { a =>
          val img = GameImg(
            src = a.attr("href"),
            index = imgIndex
          )

          imgIndex += 1
          img
        }
  }

}

class DetailPageParser {
  def parse(gameId: Int, html: String): Game = {
    val root = Jsoup.parse(html)
    val game = new DetailParser().parse(gameId, root)
    game.gameCharacters = new GameCharParser().parse(root).asJava
    game.gameImgs = new SimpleImgParser().parse(root).asJava
    game
  }
}