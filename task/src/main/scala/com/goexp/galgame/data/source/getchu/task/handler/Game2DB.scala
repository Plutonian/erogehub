package com.goexp.galgame.data.source.getchu.task.handler

import java.util

import com.goexp.common.util.string.Strings.{isEmpty, isNotEmpty}
import com.goexp.galgame.common.model.game.{GameCharacter, GameState}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
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

  private def merge(localCharList: util.List[GameCharacter], remoteCharList: util.List[GameCharacter]): util.List[GameCharacter] = {
    val localSize = Option(localCharList).map(_.size()).getOrElse(0)
    val remoteSize = Option(remoteCharList).map(_.size()).getOrElse(0)

    //do nothing
    if (localSize == 0 && remoteSize == 0) return null
    if (localSize > remoteSize) return null
    if (localSize == 0) return remoteCharList

    // make local cache
    val localCharCache = localCharList.asScala.to(LazyList).map { cc => cc.index -> cc }.toMap


    //merge local to remote
    remoteCharList.asScala.map { remoteChar =>

      //already in local
      localCharCache.get(remoteChar.index)
        .map { localChar =>

          /**
            * log
            */

          if (isEmpty(localChar.cv) && isNotEmpty(remoteChar.cv)) {
            logger.info(s"New cv ${remoteChar.cv}")
          }


          /**
            * merge local to remote
            */

          def merge(localChar: GameCharacter, remoteChar: GameCharacter): GameCharacter = {
            // copy trueCV
            val tc1 = if (isNotEmpty(localChar.trueCV)) {
              logger.trace(s"Merge trueCV ${localChar.trueCV}")
              remoteChar.copy(trueCV = localChar.trueCV)
            } else {
              remoteChar
            }


            // also copy cv
            val tc2 = if (isNotEmpty(localChar.cv)) {
              logger.trace(s"Merge cv ${localChar.cv}")
              tc1.copy(cv = localChar.cv)
            } else {
              tc1
            }

            tc2
          }

          merge(localChar, remoteChar)

        }
        //not in local
        .getOrElse(remoteChar)

    }.asJava
  }


  override def processEntity: PartialFunction[Any, Unit] = {
    case remoteGame: Game =>
      GameFullQuery().where(Filters.eq(remoteGame.id)).one() match {
        case Some(localGame) =>


          /**
            * upgrade base content
            */
          if (localGame != remoteGame) {
            //        logger.debug(s"\nOld:${localGame.simpleView}\nNew:${remoteGame.simpleView}\n")
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
            logger.debug(s"Local:${localGame.gameImgs}  Remote:${remoteGame.gameImgs}")

            logger.info(s"Update [${localGame.id}] ${localGame.name} ${localGame.state} Local:$localImgSize,Remote:$remoteImgSize")
            GameDB.updateImg(remoteGame)
          }


          // check game state
          if ((localGame.state ne GameState.SAME) && (localGame.state ne GameState.BLOCK)) {

            GameFullQuery().where(Filters.eq(remoteGame.id)).one().map { game =>
              val imgs = Util.getGameAllImgs(game)

              if (imgs.nonEmpty)
                logger.info(s"DownloadImage for Game[${localGame.id}] ${localGame.name} ${localGame.state}")

              imgs.foreach { pear =>
                sendTo[DownloadImage](pear)
              }
            }
          }

        case _ =>
      }
  }

}