package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB.StateDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.task.handler.PreProcessGame._
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

object MarkSameGameTask {

  private val logger = LoggerFactory.getLogger(MarkSameGameTask.getClass)

  def main(args: Array[String]): Unit = {
    val brandList = BrandQuery.tlp.scalaList()

    for (brand <- brandList) {
      val games = GameQuery.fullTlp
        .where(Filters.eq("brandId", brand.id))
        .scalaList().to(LazyList)

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
            value
              .groupBy {
                g =>
                  val titles = g.getTitles
                  s"${titles.mainTitle}${titles.subTitle}"
              }.to(LazyList)
              .filter {
                case (_, v) =>
                  v.size > 1
              }
              .flatMap {
                case (_, games) =>
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
