package com.goexp.galgame.data.parser

import com.goexp.galgame.common.model.TagType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._


class GetchuTagParser {
  def parse(html: String): LazyList[TagType] =
    Jsoup.parse(html)
      .select("#wrapper div.pc_headword:contains(カテゴリ一覧)")
      .first
      .parent
      .select("div.category_pc_t")
      .asScala
      .to(LazyList)
      .map(parse)

  private def parse(item: Element) = {
    val list = item
      .nextElementSibling
      .select("a")
      .asScala
      .to(LazyList)
      .map {
        _.text.trim
      }
      .toList

    TagType(item.text, 0, list)

  }
}