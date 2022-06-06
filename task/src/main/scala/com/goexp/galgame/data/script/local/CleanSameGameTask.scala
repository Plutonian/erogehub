package com.goexp.galgame.data.script.local

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.data.Config
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameSimpleQuery}
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters._


object CleanSameGameTask {
  private val logger = Logger(CleanSameGameTask.getClass)

  def remove(g: Game): Unit = {
    val path = Config.IMG_LOCAL_ROOT.resolve(GetchuGameLocal.gamePathString(g))
    logger.info(s"${g.simpleView}")
    removeR(path)
  }

  def removeR(path: Path): Unit = {
    if (Files.exists(path)) {

      logger.info(s"Image: $path")

      Files.list(path).iterator().asScala.to(LazyList)
        .foreach(Files.delete)

      Files.deleteIfExists(path)
    }
  }


  def main(args: Array[String]) = {


    logger.info("Init OK")

    BrandQuery()
      .scalaList().to(LazyList)
      .foreach {
        b =>
          val games = GameSimpleQuery()
            .where(Filters.eq("brandId", b.id))
            .scalaList().to(LazyList)

          games
            //            .filter { g => !g.isAdult }
            .filter { g => g.state eq GameState.SAME }
            .foreach {
              g =>
                //                println(g)
                remove(g)

            }


      }
  }
}