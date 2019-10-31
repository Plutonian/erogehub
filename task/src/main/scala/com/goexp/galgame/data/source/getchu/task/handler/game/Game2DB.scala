package com.goexp.galgame.data.source.getchu.task.handler.game

import java.util
import java.util.Objects

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.{GameCharacter, GameState}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.GameQuery
import com.goexp.galgame.data.source.getchu.task.Util
import com.goexp.piplline.core.{Message, MessageHandler}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

/**
  * process game detail(upgrade content,cv,simple img)
  */
class Game2DB extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[Game2DB])

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

    }.asJava
  }

  override def process(message: Message) = {
    message.entity match {
      case remoteGame: Game =>
        val localGame = GameQuery.fullTlp.where(Filters.eq(remoteGame.id)).one()

        /**
          * upgrade base content
          */
        if (!Objects.equals(localGame, remoteGame)) {
          //      logger.info(s"\nOld:${localGame.simpleView()}\nNew:${remoteGame.simpleView()}\n")
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
          logger.info("Game[{}] {}", localGame.id, localGame.name)
          logger.info(s"Update GameImg:Local:$localImgSize,Remote:$remoteImgSize")
          GameDB.updateImg(remoteGame)
        }


        // check game state
        if ((localGame.state ne GameState.SAME) && (localGame.state ne GameState.BLOCK)) {

          logger.debug("Game[{}] {}", localGame.id, localGame.state)

          val tGame = GameQuery.fullTlp.where(Filters.eq(remoteGame.id)).one()
          Util.getGameAllImgs(tGame).foreach {
            pear =>
              sendTo(classOf[DownloadImage], pear)
          }
        }
    }

  }
}