package com.goexp.galgame.data.source.erogamescape.parser

import java.nio.file.{Files, Path}

import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser._
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

object ListPageParser {
  private val GAME_ID_REGEX = """game=(\d+)#ad$""".r("id")
  private val BRAND_ID_REGEX = """brand=(\d+)$""".r("id")

  case class PageItem(id: Int, brandId: Int, name: String, middle: String, pian: String)

  def main(args: Array[String]): Unit = {

    val str = Files.readString(Path.of("/home/benbear/temp/a"))
    val parser = new ListPageParser()
    parser.parse(str)
      .foreach { g =>
        println(g)
      }

  }
}

/*
get game{
id,
name,
middle,
pian,
brandId
}

 */
class ListPageParser {

  def parse(html: String): LazyList[PageItem] = {

    def parse(element: Element): PageItem = {

      val tds = element.select("td")
      val bEle = tds.get(1).select("a").first()

      val gEle = tds.get(0).select("a").first()
      val gUrl = gEle.attr("href")
      val bUrl = bEle.attr("href")
      val id = GAME_ID_REGEX.findFirstMatchIn(gUrl).map(_.group("id").toInt).getOrElse(0)
      val brandId = BRAND_ID_REGEX.findFirstMatchIn(bUrl).map(_.group("id").toInt).getOrElse(0)
      val name = gEle.text()
      val middle = tds.get(2).text()
      val pian = tds.get(3).text()

      PageItem(id,
        brandId,
        name,
        middle,
        pian
      )


    }

    Jsoup.parse(html)
      .select(".recently_game_list table.toukei_table")
      .asScala.to(LazyList)
      .flatMap { s => s.select("tr").asScala.drop(1) }
      .map(parse)


  }
}