package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB.StateDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.model.Game
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.io.{Codec, Source}
import scala.jdk.CollectionConverters._

object MarkSameGameTask {

  private val logger = LoggerFactory.getLogger(MarkSameGameTask.getClass)

  private lazy val samelist = {
    val source = Source.fromInputStream(MarkSameGameTask.getClass.getResourceAsStream("/same.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }

  private lazy val packagelist = {
    val source = Source.fromInputStream(MarkSameGameTask.getClass.getResourceAsStream("/package.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }


  private def isSameGame = (game: Game) => samelist.exists(str => game.name.contains(str))

  private def isPackageGame = (game: Game) =>
    Option(game.`type`).map(_.asScala).getOrElse(List.empty).contains("セット商品") ||
      packagelist.exists(str => game.name.contains(str))

  def main(args: Array[String]): Unit = {
    val brandList = BrandQuery.tlp.query.scalaList

    for (brand <- brandList) {
      val games = GameQuery.fullTlp.query
        .where(Filters.eq("brandId", brand.id))
        .scalaList.to(LazyList)

      games
        .groupBy { game =>
          if (isSameGame(game)) "same"
          else if (isPackageGame(game)) "package"
          else "other"
        }
        .flatMap {
          case ("same", value: LazyList[Game]) =>
            value.filter(_.state eq GameState.UNCHECKED).map { game => game.state = GameState.SAME; game }
          case ("package", value: LazyList[Game]) =>
            value.filter(_.state eq GameState.UNCHECKED).map { game => game.state = GameState.PACKAGE; game }
          case ("other", value: LazyList[Game]) =>
            value.filter {
              _.publishDate != null
            }.groupBy {
              _.publishDate
            }.values
              .filter {
                _.size > 1
              }
              .flatMap {
                games =>
                  //games by date

                  games.sortBy {
                    _.name.length
                  }
                    .drop(1)
                    .filter {
                      _.state eq GameState.UNCHECKED
                    }
                    .map { game => game.state = GameState.SAME; game }

              }
          //        case _ =>
          //          throw new RuntimeException("Error")
        }
        .foreach {
          case game: Game =>
            logger.info(s"ID:${game.id} Name: ${game.name}  State: ${game.state}")
            StateDB.update(game)
          //        case _ =>
        }
    }
  }

}
