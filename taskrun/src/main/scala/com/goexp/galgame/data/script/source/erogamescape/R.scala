package com.goexp.galgame.data.script.source.erogamescape

import com.goexp.galgame.data.source.getchu.query.GameQuery
import com.mongodb.client.model.Filters


object R {

  def main(args: Array[String]): Unit = {
    GameQuery.simpleTlp.where(
      //      Filters.add()
      Filters.exists("group")).scalaList().to(LazyList)
      .groupBy { g => g.group }
      .foreach { case (k, v) =>
        println(s" --> $k")

        v.foreach {
          g =>
            println(s"[${g.id}] ${g.name}  ${g.brandId}")
        }

      }

  }

}
