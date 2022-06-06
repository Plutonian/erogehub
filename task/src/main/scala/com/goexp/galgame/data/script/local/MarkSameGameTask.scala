package com.goexp.galgame.data.script.local

import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.actor.InsertOrUpdateGameActor._
import com.goexp.galgame.data.source.getchu.importor.GameDB.StateDB
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameFullQuery}
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

object MarkSameGameTask {

  private val logger = Logger(MarkSameGameTask.getClass)

  def main(args: Array[String]): Unit = {
    val brandList = BrandQuery().scalaList()

    for (brand <- brandList) {
      val games = GameFullQuery()
        .where(Filters.eq("brandId", brand.id))
        .scalaList().to(LazyList)

      games
        .groupBy { game =>
          if (isSameGame(game)) "same"
//          else if (isPackageGame(game)) "package"
          else "other"
        }
        .flatMap {
          case ("same", value: LazyList[Game]) =>
            value.filter(_.state eq GameState.UNCHECKED).map { game => game.state = GameState.SAME; game }
//          case ("package", value: LazyList[Game]) =>
//            value.filter(_.state eq GameState.UNCHECKED).map { game => game.state = GameState.PACKAGE; game }
          case ("other", value: LazyList[Game]) =>
            value
              .groupBy {
                g =>
                  val Titles(mainTitle, subTitle) = g.getTitles
                  s"${mainTitle}${subTitle}"
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
            logger.info(s"${game.simpleView}")
            StateDB.update(game)
          //        case _ =>
        }
    }
  }

}
