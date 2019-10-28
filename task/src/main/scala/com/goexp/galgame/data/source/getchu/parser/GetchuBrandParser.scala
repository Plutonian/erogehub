package com.goexp.galgame.data.source.getchu.parser

import com.goexp.galgame.data.model.Brand
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

private object GetchuBrandParser {
  private val ID_REGEX = """search_brand_id=(\d+)""".r("id")

}

class GetchuBrandParser {

  def parse(html: String): LazyList[Brand] = {
    Jsoup.parse(html)
      .select("#wrapper > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td table[cellpadding=1]")
      .asScala
      .to(LazyList)
      .drop(1)
      .flatMap(parse)
  }


  import GetchuBrandParser._

  private def parse(item: Element) =

    item.nextElementSibling
      .select("tr:nth-of-type(2) tr")
      .asScala
      .to(LazyList)
      .filter(_.html.length > 0)
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