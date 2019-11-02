package com.goexp.galgame.data.source.getchu.task.handler

import java.util
import java.util.Objects

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.{GameCharacter, GameState}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.GameQuery
import com.goexp.galgame.data.source.getchu.task.Util
import com.goexp.piplline.handler.DefaultHandler
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

/**
  * process game detail(upgrade content,cv,simple img)
  */
class Game2DB extends DefaultHandler {
  final private val logger = Logger(classOf[Game2DB])

  private def merge(local: util.List[GameCharacter], remote: util.List[GameCharacter]): util.List[GameCharacter] = {
    val localSize = Option(local).map(_.size()).getOrElse(0)
    val remoteSize = Option(remote).map(_.size()).getOrElse(0)

    //do nothing
    if (localSize == 0 && remoteSize == 0) return null
    if (localSize > remoteSize) return null
    if (localSize == 0) return remote

    // make local cache
    val localMap = local.asScala.to(LazyList).map { cc => cc.index -> cc }.toMap
    //merge local to remote
    remote.asScala.map { rc =>
      localMap.get(rc.index) match {
        case Some(localC) =>

          /**
            * merge local to remote
            */

          // copy trueCV
          if (Strings.isNotEmpty(localC.trueCV)) {
            logger.trace(s"Merge trueCV ${rc}")
            rc.trueCV = localC.trueCV
          }
          // also copy cv
          if (Strings.isNotEmpty(localC.cv)) {
            logger.trace(s"Merge cv ${rc}")
            rc.cv = localC.cv
          }

          /**
            * log
            */

          if (Strings.isEmpty(localC.cv) && Strings.isNotEmpty(rc.cv))
            logger.info(s"New cv ${rc.cv}")


        case _ =>
      }

      rc

    }.asJava
  }


  override def processEntity: PartialFunction[Any, Unit] = {
    case remoteGame: Game =>
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
      remoteGame.gameCharacters = merge(localGame.gameCharacters, remoteGame.gameCharacters)

      if (remoteGame.gameCharacters != null)
        GameDB.updateChar(remoteGame)


      /**
        * upgrade simple img
        */
      val localImgSize = Option(localGame.gameImgs).map(_.size).getOrElse(0)
      val remoteImgSize = Option(remoteGame.gameImgs).map(_.size).getOrElse(0)

      if (remoteImgSize > localImgSize) {
        logger.info(s"Game[${localGame.id}] ${localGame.name} ${localGame.state}")
        logger.info(s"Update GameImg:Local:$localImgSize,Remote:$remoteImgSize")
        GameDB.updateImg(remoteGame)
      }


      // check game state
      if ((localGame.state ne GameState.SAME) && (localGame.state ne GameState.BLOCK)) {

        logger.info(s"Downloading for Game[${localGame.id}] ${localGame.name} ${localGame.state}")

        val tGame = GameQuery.fullTlp.where(Filters.eq(remoteGame.id)).one()
        Util.getGameAllImgs(tGame).foreach {
          pear =>
            sendTo(classOf[DownloadImage], pear)
        }
      }

  }

}