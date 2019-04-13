package com.goexp.galgame.data.parser

import java.util.Objects

import com.goexp.galgame.common.model.CommonGame
import org.jsoup.Jsoup

import scala.collection.JavaConverters._


object GameGuideParser {

  class Sagaoz_Net {

    def parse(html: String) = {
      Objects.requireNonNull(html)
      Jsoup.parse(html)
        .select("table[width=720]:not(:nth-of-type(1))")
        .select("a")
        .asScala
        .toStream
        .filter(a => !a.attr("href").isEmpty)
        .map(a => {
          val guide = new CommonGame.Guide
          guide.title = a.text
          guide.from = CommonGame.Guide.DataFrom.sagaoz_net
          guide.href = a.attr("href")
          guide.id = guide.href
          guide

        })
        .toSet
    }
  }

  class Seiya_saiga {

    def parse(html: String) = {
      Objects.requireNonNull(html)
      Jsoup.parse(html)
        .select("body > div > table > tbody > tr > th> table")
        .asScala
        .toStream
        .drop(4)
        .flatMap(node => {
          node.select("a")
            .asScala
            .toStream
            .map(a => {
              val guide = new CommonGame.Guide
              guide.title = a.text
              guide.from = CommonGame.Guide.DataFrom.seiya_saiga_com
              guide.href = s"http://seiya-saiga.com/game/${a.attr("href")}"
              guide.id = guide.href
              guide
            })

        })
        .toSet
    }
  }

}