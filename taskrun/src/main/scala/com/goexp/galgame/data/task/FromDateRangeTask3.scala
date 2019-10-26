package com.goexp.galgame.data.task

import java.time.LocalDate
import java.util.Objects
import java.util.concurrent.{CompletableFuture, TimeUnit}

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.{CommonGame, GameState}
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.common.website.getchu.{GetchuGameRemote, RequestBuilder}
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.parser.game.DetailPageParser
import com.goexp.galgame.data.task.client.GetChu
import com.goexp.galgame.data.task.client.GetChu.GameRemote
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object FromDateRangeTask3 {
  private val logger = LoggerFactory.getLogger(FromDateRangeTask3.getClass)


  private[this] def game2DB(remoteGame: Game): Unit = {


    def merge(local: List[CommonGame.GameCharacter], remote: List[CommonGame.GameCharacter]): List[CommonGame.GameCharacter] = {
      val localSize = Option(local).map(_.size).getOrElse(0)
      val remoteSize = Option(remote).map(_.size).getOrElse(0)

      //do nothing
      if (localSize == 0 && remoteSize == 0) return null
      if (localSize > remoteSize) return null
      if (localSize == 0) return remote

      // make local cache
      val localMap = local.to(LazyList).map { cc => cc.index -> cc }.toMap
      //merge local to remote
      remote.map { rc =>
        localMap.get(rc.index) match {
          case Some(localC) =>

            /**
              * merge local to remote
              */

            // copy trueCV
            if (Strings.isNotEmpty(localC.trueCV)) {
              logger.trace("Merge trueCV {}", rc)
              rc.trueCV = localC.trueCV
            }
            // also copy cv
            if (Strings.isNotEmpty(localC.cv)) {
              logger.trace("Merge cv {}", rc)
              rc.cv = localC.cv
            }

            /**
              * log
              */

            if (Strings.isEmpty(localC.cv) && Strings.isNotEmpty(rc.cv))
              logger.info("New cv {}", rc.cv)


          case _ =>
        }

        rc

      }
    }

    val localGame = GameQuery.fullTlp.where(Filters.eq(remoteGame.id)).one()


    /**
      * upgrade base content
      */
    if (!Objects.equals(localGame, remoteGame)) {
      logger.debug(s"\nOld:${localGame.simpleView}\nNew:${remoteGame.simpleView}\n")
      GameDB.updateAll(remoteGame)
    }


    /**
      * upgrade person
      */
    remoteGame.gameCharacters = merge(localGame.gameCharacters.asScala.toList, remoteGame.gameCharacters.asScala.toList).asJava

    if (remoteGame.gameCharacters != null)
      GameDB.updateChar(remoteGame)


    /**
      * upgrade simple img
      */
    val localImgSize = Option(localGame.gameImgs).map(_.size).getOrElse(0)
    val remoteImgSize = Option(remoteGame.gameImgs).map(_.size).getOrElse(0)

    if (remoteImgSize > localImgSize) {
      logger.info("Game:{}", remoteGame.id)
      logger.info(s"Update GameImg:Local:$localImgSize,Remote:$remoteImgSize")
      GameDB.updateImg(remoteGame)
    }

  }

  private[this] def getBytesAsy(gameId: Int) = {
    val request = RequestBuilder(GetchuGameRemote.byId(gameId)).adaltFlag.build
    GetChu.getHtmlAsy(request)
  }

  def main(args: Array[String]): Unit = {

    Network.initProxy()

    /**
      * Get list --> Pre process -->download game detail -->parse html to game --> save to db
      */

    val start = LocalDate.now.minusMonths(1).withDayOfMonth(1)

    val end = start.plusMonths(6)
    //    val end = LocalDate.now.withMonth(12).withDayOfMonth(31)

    logger.info(s"Start:$start,End:$end")


    /*
    Get remote list(only list)
     */
    val remoteGameList = GameRemote.from(start, end)

    logger.info(s"${remoteGameList.size}")


    /*
    Pre process game
     */

    var num = 0

    val futures = remoteGameList
      .map { game =>
        num += 1

        if (num % 10 == 0)
          TimeUnit.SECONDS.sleep(5L)


        //already has
        if (GameDB.exist(game.id)) {
          logger.debug("<Update> {}", game.simpleView)
          GameDB.update(game)
        }
        else {
          //new game

          game.state = GameState.UNCHECKED
          game.isNew = true
          logger.info("<Insert> {}", game.simpleView)
          GameDB.insert(game)
        }

        game

      }
      /*
      Download game
       */
      .map { game =>
        getBytesAsy(game.id)
          .exceptionally { e =>
            logger.error(s"${e.getCause.getMessage}", e.getCause)
            null
          }
          .thenAccept { html =>

            /*
            Parse
             */
            val parser = new DetailPageParser
            val remoteGame = parser.parse(game.id, html)

            /*
            Save to db
             */
            game2DB(remoteGame)
          }
      }


    CompletableFuture.allOf(futures: _*).join()

  }


}