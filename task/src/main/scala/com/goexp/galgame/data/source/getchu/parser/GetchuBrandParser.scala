package com.goexp.galgame.data.source.getchu.parser

import com.goexp.galgame.data.model.Brand
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

private object GetchuBrandParser {
  private val ID_REGEX = """search_brand_id=(?<id>\d+)""".r

}

class GetchuBrandParser {

  def parse(html: String): LazyList[Brand] = {
    Jsoup.parse(html)
      .select("table.brand_half_table")
      .asScala
      .to(LazyList)
      .flatMap(parse)
  }


  import GetchuBrandParser._

  private def parse(item: Element) =

    item
      .select("tr:nth-of-type(2) tr")
      .asScala
      .to(LazyList)
      .filter(_.html.nonEmpty)
      .map { ele =>
        val brand = new Brand
        val titleEle = ele.select("td:nth-of-type(1)>a")
        val idUrl = titleEle.attr("href")
        brand.name = titleEle.text
        brand.id = ID_REGEX.findFirstMatchIn(idUrl).map {
          _.group("id").toInt
        }.getOrElse(0)
        val websiteEle = ele.select("td:nth-of-type(2)>a")
        brand.website = websiteEle.attr("href")
        //                    brand.index = index;
        brand
      }
}