package com.goexp.galgame.data.source.getchu.actor

import java.util

import com.goexp.common.util.Logger
import com.goexp.common.util.string.StringOption
import com.goexp.common.util.string.Strings.{isEmpty, isNotEmpty}
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.goexp.piplline.handler.DefaultActor
import com.mongodb.client.model.Filters

import scala.jdk.CollectionConverters._

/**
 * process game detail(upgrade content,cv,simple img)
 */

private object SaveGameActor extends Logger {

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

          if (isEmpty(localChar.cv) && isNotEmpty(remoteChar.cv)) {
            logger.info(s"New cv ${remoteChar.cv}")
          }

          def merge(localChar: GameCharacter, remoteChar: GameCharacter): GameCharacter = {

            remoteChar.copy(
              trueCV = StringOption(localChar.trueCV).getOrElse(remoteChar.trueCV)
              ,
              cv = StringOption(localChar.cv).getOrElse(remoteChar.cv)
            )

          }

          merge(localChar, remoteChar)
        }
        //not in local
        .getOrElse(remoteChar)

    }.asJava
  }
}

class SaveGameActor extends DefaultActor {


  override def receive = {
    case remoteGame: Game =>
      GameFullQuery().where(Filters.eq(remoteGame.id)).one() match {
        case Some(localGame) =>


          /**
           * upgrade base content
           */
          if (localGame != remoteGame) {
            GameDB.updateAll(remoteGame)
            logger.info(s"Update Basic [${localGame.id}] ${localGame.name} ")
          }

          import SaveGameActor.merge

          /**
           * upgrade person
           */
          remoteGame.gameCharacters = merge(localGame.gameCharacters, remoteGame.gameCharacters)

          if (remoteGame.gameCharacters != null) {
            GameDB.updateChar(remoteGame)
            logger.debug(s"Update Char [${localGame.id}] ${localGame.name} ")
          }


          /**
           * upgrade sample img
           */
          val localImgSize = Option(localGame.gameImgs).map(_.size).getOrElse(0)
          val remoteImgSize = Option(remoteGame.gameImgs).map(_.size).getOrElse(0)

          if (remoteImgSize > localImgSize) {
            //            logger.debug(s"[${localGame.id}] ${localGame.name} Local:${localGame.gameImgs}  Remote:${remoteGame.gameImgs}")

            logger.info(s"Update Sample Img[${localGame.id}] ${localGame.name} ${localGame.state} Local:$localImgSize,Remote:$remoteImgSize")
            GameDB.updateImg(remoteGame)
          }

          // check game state
          sendTo[CheckStateActor](localGame)

        case _ =>
      }
  }

}