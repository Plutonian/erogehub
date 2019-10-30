package com.goexp.galgame.data.script.source.erogamescape

import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser.{BasicItem, OutLink}
import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser.PageItem
import com.goexp.galgame.data.source.erogamescape.query.GameQuery
import com.goexp.galgame.data.source.erogamescape.query.GameQuery.ParseLine
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.mongodb.client.model.Filters

object R2 {

  def main(args: Array[String]): Unit = {
    GameQuery.gameItemTlp.where(Filters.ne("getchuId", 0)).scalaList().to(LazyList)
      .groupBy { case ParseLine(_, BasicItem(OutLink(_, getchuId, _), _, _, _)) => getchuId }.to(LazyList)
      .filter { case (k, v) => GameDB.exist(k) && v.size == 1 }
      .flatMap {
        case (k, v) => v
      }
      .foreach {
        case ParseLine(PageItem(id, _, name, middle, _), BasicItem(OutLink(gHP, getchuId, _), _, group, _)) =>
          println(s" = $getchuId =>[${id}] $name $gHP")

          GameDB.update(getchuId, middle, gHP, group)
      }
  }

}
