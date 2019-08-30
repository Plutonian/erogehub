package com.goexp.galgame.data.task.local

import java.nio.file.Files

import com.goexp.galgame.common.Config
import com.goexp.galgame.common.model.{BrandType, GameState}
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object CleanGameTask {
  private val logger = LoggerFactory.getLogger(CleanGameTask.getClass)


  def main(args: Array[String]) = {

    def remove(g: Game): Unit = {
      val path = Config.DATA_ROOT.resolve("img/game/").resolve(String.valueOf(g.id))
      if (Files.exists(path)) {

        logger.info(s"Clean:$path")

        Files.list(path).iterator().asScala.to(LazyList)
          .foreach(Files.delete)

        Files.deleteIfExists(path)
      }
    }

    logger.info("Init OK")

    BrandQuery.tlp.query
      .list.asScala.to(LazyList)
      .foreach(b => {
        if (b.isLike eq BrandType.BLOCK) {
          GameDB.blockAllGame(b)

          GameQuery.simpleTlp.query
            .where(Filters.eq("brandId", b.id))
            .list.asScala.to(LazyList)
            .foreach(g => {
              remove(g)
            })
        } else {
          GameQuery.simpleTlp.query
            .where(Filters.eq("brandId", b.id))
            .list.asScala.to(LazyList)
            .filter(g => (g.state eq GameState.BLOCK) || (g.state eq GameState.SAME))
            .foreach(g => {
              remove(g)
            })
        }


      })
  }
}