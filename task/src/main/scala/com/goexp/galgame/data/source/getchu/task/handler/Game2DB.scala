package com.goexp.galgame.data.source.getchu.task.handler

import java.util

import com.goexp.common.util.string.Strings
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
      localMap.get(rc.index)
        .map { localC =>

          /**
            * merge local to remote
            */

          var cc = rc

          // copy trueCV
          if (Strings.isNotEmpty(localC.trueCV)) {
            logger.trace(s"Merge trueCV ${cc}")
            cc = cc.copy(trueCV = localC.trueCV) // = localC.trueCV
          }
          // also copy cv
          if (Strings.isNotEmpty(localC.cv)) {
            logger.trace(s"Merge cv ${cc}")
            cc = cc.copy(cv = localC.cv)
          }

          /**
            * log
            */

          if (Strings.isEmpty(localC.cv) && Strings.isNotEmpty(cc.cv))
            logger.info(s"New cv ${cc.cv}")

          cc
        }.getOrElse(rc)

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