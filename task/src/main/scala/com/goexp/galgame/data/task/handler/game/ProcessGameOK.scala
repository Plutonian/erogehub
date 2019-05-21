package com.goexp.galgame.data.task.handler.game

import java.util
import java.util.Objects

import com.goexp.common.util.Strings
import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


class ProcessGameOK extends DefaultMessageHandler[Game] {
  final private val logger = LoggerFactory.getLogger(classOf[ProcessGameOK])

  private def merge(local: util.List[CommonGame.GameCharacter], remote: util.List[CommonGame.GameCharacter]): util.List[CommonGame.GameCharacter] = {
    if (local == null && remote == null) return null
    if (local == null) return remote
    // make local cache
    val localMap = local.asScala.toStream.map(cc => cc.index -> cc).toMap
    //merge local to remote
    remote.asScala.map((rc: CommonGame.GameCharacter) => {
      localMap.get(rc.index) match {
        case Some(localC) =>
          if (Strings.isNotEmpty(localC.trueCV)) {
            logger.debug("Merge trueCV {}", rc)
            rc.trueCV = localC.trueCV
          }
          // also copy cv
          if (Strings.isNotEmpty(localC.cv)) {
            logger.debug("Merge cv {}", rc)
            rc.cv = localC.cv
          }
        case _ =>
      }

      rc
    }).asJava
  }

  override def process(message: Message[Game]) = {
    val remoteGame = message.entity
    logger.debug("Process {}", remoteGame)
    val localGame = GameQuery.fullTlp.query.where(Filters.eq(remoteGame.id)).one

    if (!Objects.equals(localGame, remoteGame)) {
      logger.info(s"\nOld:${localGame.simpleView()}\nNew:${remoteGame.simpleView()}\n")
      GameDB.updateAll(remoteGame)
    }

    remoteGame.gameCharacters = merge(localGame.gameCharacters, remoteGame.gameCharacters)

    if (remoteGame.gameCharacters != null)
      GameDB.updateChar(remoteGame)

    val localImgSize = Option(localGame.gameImgs).map(_.size).getOrElse(0)
    val remoteImgSize = Option(remoteGame.gameImgs).map(_.size).getOrElse(0)

    if (remoteImgSize > localImgSize) {
      logger.info("Game:{}", remoteGame.id)
      logger.info(s"Update GameImg:Local:$localImgSize,Remote:$remoteImgSize")
      GameDB.updateImg(remoteGame)
    }
  }
}