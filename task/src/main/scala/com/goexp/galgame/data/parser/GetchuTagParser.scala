package com.goexp.galgame.data.parser

import com.goexp.galgame.common.model.TagType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._


class GetchuTagParser {
  def parse(html: String): Stream[TagType] =
    Jsoup.parse(html)
      .select("#wrapper div.pc_headword:contains(カテゴリ一覧)")
      .first
      .parent
      .select("div.category_pc_t")
      .asScala
      .toStream
      .map(parse)

  private def parse(item: Element) = {
    val tagType = new TagType
    tagType.`type` = item.text
    tagType.order = 0
    tagType.tags = item
      .nextElementSibling
      .select("a")
      .asScala
      .toStream
      .map(ele => ele.text.trim)
      .asJava

    tagType
  }
}