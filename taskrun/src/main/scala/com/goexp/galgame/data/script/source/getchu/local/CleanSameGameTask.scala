package com.goexp.galgame.data.script.source.getchu.local

import java.nio.file.Files

import com.goexp.galgame.common.Config
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameSimpleQuery}
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._


object CleanSameGameTask {
  private val logger = Logger(CleanSameGameTask.getClass)


  def main(args: Array[String]) = {

    def remove(g: Game): Unit = {
      val path = Config.IMG_PATH.resolve(String.valueOf(g.id))
      if (Files.exists(path)) {

        logger.info(s"[${g.id}] ${g.name} [${g.state}]")
        logger.info(s"Clean:$path")

        Files.list(path).iterator().asScala.to(LazyList)
          .foreach(Files.delete)

        Files.deleteIfExists(path)
      }
    }

    logger.info("Init OK")

    BrandQuery.tlp
      .scalaList().to(LazyList)
      .foreach {
        b =>
          val games = GameSimpleQuery()
            .where(Filters.eq("brandId", b.id))
            .scalaList().to(LazyList)

          games
            .filter { g => (g.state eq GameState.SAME) || (g.state eq GameState.BLOCK) }
            .foreach {
              g =>
                remove(g)

            }


      }
  }
}