package com.goexp.galgame.data.parser

import java.util
import java.util.regex.Pattern

import com.goexp.galgame.data.model.Brand
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._

class GetchuBrandParser {
  def parse(html: String): util.List[Brand] = {
    //    val list = new util.ArrayList[Brand]
    Jsoup.parse(html)
      .select("#wrapper > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td table[cellpadding=1]")
      .asScala
      .toStream
      .drop(1)
      .flatMap(parse)
      .asJava
  }

  private def parse(item: Element) =

    item.nextElementSibling
      .select("tr:nth-of-type(2) tr")
      .asScala
      .toStream
      .filter(_.html.length > 0)
      .map((ele: Element) => {
        val brand = new Brand
        val titleEle = ele.select("td:nth-of-type(1)>a")
        val idUrl = titleEle.attr("href")
        brand.name = titleEle.text
        val m = Pattern.compile("search_brand_id=(?<id>\\d+)").matcher(idUrl)
        brand.id = if (m.find) m.group("id").toInt
        else 0
        val websiteEle = ele.select("td:nth-of-type(2)>a")
        brand.website = websiteEle.attr("href")
        //                    brand.index = index;
        brand
      })
}