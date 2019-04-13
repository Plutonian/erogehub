package com.goexp.galgame.data.parser.game

import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.data.model.Game
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import scala.collection.JavaConverters._

object DetailPageParser {

  private object DetailPartParser {
    private val selector = "#soft_table >tbody>tr:nth-of-type(2)"
    private val BRAND_ID_REX = "search_brand_id=(?<brandid>\\d+)$".r
  }

  private class DetailPartParser {
    def parse(gameId: Int, root: Document) = {
      val ele = root.select(DetailPartParser.selector)
      val g = new Game
      g.id = gameId
      g.painter = ele.select("td:contains(原画)").next.text.split("、").toStream.map(s => s.trim).asJava
      g.writer = ele.select("td:contains(シナリオ)").next.text.split("、").toStream.map(s => s.trim).asJava
      g.`type` = ele.select("td:contains(サブジャンル)").next.text.replace("[一覧]", "").split("、").toStream.map(s => s.trim).asJava
      g.tag = ele.select("td:contains(カテゴリ)").next.text.replace("[一覧]", "").split("、").toStream.map(s => s.trim).asJava
      g.story = root.select("#wrapper div.tabletitle:contains(ストーリー)").next.html.replaceAll("\\<[^\\>]*\\>", "").replace("<br>", "").trim
      g.intro = root.select("#wrapper div.tabletitle:contains(商品紹介)").next.html.replaceAll("\\<[^\\>]*\\>", "").replace("<br>", "").trim
      val brandUrl = ele.select("a:contains(このブランドの作品一覧)").attr("href")
      g.brandId = DetailPartParser.BRAND_ID_REX.findFirstMatchIn(brandUrl).map(m => m.group("brandid").toInt).getOrElse(0)
      g
    }
  }

  private object GameCharPartParser {
    private val cvPattern = "（?[Cc][vV]\\s*[：:\\.／/]?\\s*(?<cv>[^）]+)）?$".r
  }

  private class GameCharPartParser {
    private var index = 1

    private def parseName(str: String) = str.replaceAll("（?[Cc][vV][：:\\.／/]?）?", "").trim

    private def parseCV(str: String) = {
      GameCharPartParser.cvPattern.findFirstMatchIn(str).map(m => m.group("cv").trim).getOrElse("")
    }

    def parse(root: Document) =
      root.select("#wrapper div.tabletitle:contains(キャラクター)").next.select("tbody>tr:nth-of-type(2n+1)").asScala
        .toStream
        .map((tr: Element) => {
          val gameCharacter = new CommonGame.GameCharacter
          gameCharacter.index = index
          val title = tr.select("h2.chara-name").text
          gameCharacter.img = tr.select("td:nth-of-type(1)>img").attr("src")
          gameCharacter.cv = parseCV(title)
          gameCharacter.trueCV = ""
          gameCharacter.name = parseName(title.replace(gameCharacter.cv, ""))
          gameCharacter.intro = tr.select("dl dd").html.replaceAll("\\<[^\\>]*\\>", "").trim
          index += 1
          gameCharacter
        })
  }

  private class SimpleImgPartParser {
    private var imgIndex = 1

    def parse(root: Document) =
      root.select("#wrapper div.tabletitle:contains(サンプル画像)").next.select("a.highslide")
        .asScala.toStream
        .map(a => {
          val img = new CommonGame.GameImg
          img.src = a.attr("href")
          img.index = imgIndex
          imgIndex += 1
          img
        })
  }

}

class DetailPageParser {
  def parse(gameId: Int, html: String): Game = {
    val root = Jsoup.parse(html)
    val game = new DetailPageParser.DetailPartParser().parse(gameId, root)
    game.gameCharacters = new DetailPageParser.GameCharPartParser().parse(root).asJava
    game.gameImgs = new DetailPageParser.SimpleImgPartParser().parse(root).asJava
    game
  }
}