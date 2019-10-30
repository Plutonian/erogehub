package com.goexp.galgame.data.source.erogamescape.parser

import java.nio.file.{Files, Path}
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util

import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser.Tags
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object DetailPageParser {

  private val GETCHU_ID_REX = """id=(\d+)$""".r("id")
  private val DMM_ID_REX = """detail%2F([^%]+)%2F""".r("id")

  private val logger = LoggerFactory.getLogger(DetailPageParser.getClass)

  case class OutLink(gHP: String,
                     getchuId: Int,
                     DMMID: String)

  def parseOutLink(html: String): OutLink = {

    val root = Jsoup.parse(html)

    val ele = root.select("#bottom_inter_links_main")

    logger.debug("{}", ele)

    val gHP = ele.select("a:contains(game_OHP)").attr("href")
    val getchu = ele.select("a:contains(Getchu.com)").attr("href")

    val getchuId = DetailPageParser.GETCHU_ID_REX.findFirstMatchIn(getchu).map(m => m.group("id").trim.toInt).getOrElse(0)
    val DMM = ele.select("a:contains(DMM)").attr("href")

    val DMMID = DetailPageParser.DMM_ID_REX.findFirstMatchIn(DMM).map(m => m.group("id").trim).orNull

    logger.debug(gHP)
    logger.debug(s"$getchuId")
    logger.debug(DMMID)

    OutLink(
      gHP,
      getchuId,
      DMMID
    )

  }

  case class Tags(k: String, v: util.List[String])

  def parseTag(html: String) = {
    val root = Jsoup.parse(html)
    root.select("#att_pov_table tr").asScala.to(LazyList)
      .drop(1)
      .map { tr =>
        val k = tr.select("th").text()
        val v = tr.select("td>a").asScala.to(LazyList).map { a => a.text() }.asJava
        Tags(k, v)
      }
  }

  def parseDate(html: String): LocalDate = {
    val root = Jsoup.parse(html)
    Option(root.select("#sellday td").first()).map { td =>
      try LocalDate.parse(td.text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) catch {
        case _: DateTimeParseException => null
      }
    }.orNull
  }

  def parseGroup(html: String): String = {
    val root = Jsoup.parse(html)
    Option(root.select("#gamegroup >ul>li>a").first()).map {
      _.text()
    }.orNull
  }

  case class BasicItem(outLink: OutLink, tags: LazyList[Tags], group: String, date: LocalDate)

  def parse(html: String): BasicItem = {

    BasicItem(parseOutLink(html), parseTag(html), parseGroup(html), parseDate(html))
  }

  //  private object GameCharParser {
  //    private val cvPattern = """（?[Cc][vV]\s*[：:.／/]?\s*([^）]+)）?$""".r("cv")
  //  }
  //
  //  private class GameCharParser {
  //    private var index = 1
  //
  //    private def parseName(str: String) = str.replaceAll("（?[Cc][vV][：:.／/]?）?", "").trim
  //
  //    private def parseCV(str: String) = {
  //      import GameCharParser._
  //
  //      cvPattern.findFirstMatchIn(str).map(m => m.group("cv").trim).getOrElse("")
  //    }
  //
  //    def parseOutLink(root: Document) =
  //      root.select("#wrapper div.tabletitle:contains(キャラクター)").next.select("tbody>tr:nth-of-type(2n+1)").asScala
  //        .to(LazyList)
  //        .map { tr =>
  //          val person = new GameCharacter
  //          person.index = index
  //          val title = tr.select("h2.chara-name").text
  //          person.img = tr.select("td:nth-of-type(1)>img").attr("src")
  //          person.cv = parseCV(title)
  //          person.trueCV = ""
  //          person.name = parseName(title.replace(person.cv, ""))
  //          person.intro = tr.select("dl dd").html.replaceAll("<[^>]*>", "").trim
  //          index += 1
  //          person
  //        }
  //  }
  //
  //  private class SimpleImgParser() {
  //    private var imgIndex = 1
  //
  //    def parseOutLink(root: Document) =
  //      root.select("#wrapper div.tabletitle:contains(サンプル画像)").next.select("a.highslide")
  //        .asScala.to(LazyList)
  //        .map { a =>
  //          val img = new GameImg
  //          img.src = a.attr("href")
  //          img.index = imgIndex
  //          imgIndex += 1
  //          img
  //        }
  //  }

}

object Runner {
  def main(args: Array[String]): Unit = {
    val str = Files.readString(Path.of("/home/benbear/temp/content.html"))

    println(DetailPageParser.parseOutLink(str))
    DetailPageParser.parseTag(str)
      .foreach {
        case Tags(k, v) =>
          println(k, v)
      }

    println(DetailPageParser.parseGroup(str))
    println(DetailPageParser.parseDate(str))
  }
}

//class DetailPageParser {
//  def parse(html: String) = {
//    val root = Jsoup.parse(html)
//    new DetailParser().parseOutLink(root)
//  }
//
//  def parseTag(html: String) = {
//    val root = Jsoup.parse(html)
//    new DetailParser().parseTag(root)
//  }
//}