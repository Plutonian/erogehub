package com.goexp.galgame.data.script.local

import com.goexp.galgame.data.source.getchu.query.GameSimpleQuery
import com.mongodb.client.model.Filters

import java.nio.file.{Files, Path}

object CleanImageTask {

  def main(args: Array[String]): Unit = {

    Files.list(Path.of("/home/benbear/nas/eroge_data/img/")).forEach(parentP => {

      Files.list(parentP).forEach(p => {
        Files.list(p).forEach(p => {

          val exists = GameSimpleQuery().where(Filters.eq(p.getFileName.toString.toInt)).exists

          if (!exists) {
            println(p, p.getFileName.toString.toInt, exists)
            CleanSameGameTask.removeR(p)
          }
        })
      })
    })
  }

}
