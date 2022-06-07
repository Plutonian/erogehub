package com.goexp.galgame.data.source.getchu.actor

import com.goexp.common.cache.SimpleCache
import com.goexp.common.util.Logger
import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.StringOption
import com.goexp.common.util.string.Strings.{isEmpty, isNotEmpty}
import com.goexp.galgame.common.model.game.{GameCharacter, GameState}
import com.goexp.galgame.data.model.{Brand, Game}
import com.goexp.galgame.data.source.getchu.actor.InsertOrUpdateGameActor.isSameGame
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameFullQuery}
import com.goexp.pipeline.handler.DefaultActor
import com.mongodb.client.model.Filters

import java.util
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

          remoteGame.state = localGame.state

          //Mark game is spec
          if (!(localGame.state eq GameState.SAME)) {
            if (isSameGame(remoteGame)) {
              remoteGame.state = GameState.SAME

              logger.info(s"Mark SAME!!! ${remoteGame.simpleView}")

            } else {
              logger.debug(s"Not SAME!!! ${remoteGame.simpleView}")

            }
          }

          /**
           * upgrade base content
           */
          GameDB.updateAll(remoteGame)
          if (!GameState.ignoreState().contains(remoteGame.state))
            logger.info(s"Update Basic ${localGame.simpleView} ")

          import SaveGameActor.merge

          /**
           * upgrade person
           */
          remoteGame.gameCharacters = merge(localGame.gameCharacters, remoteGame.gameCharacters)

          if (remoteGame.gameCharacters != null) {
            GameDB.updateChar(remoteGame)
            val localCharSize = Option(localGame.gameCharacters).map(_.size()).getOrElse(0)
            val remoteCharSize = remoteGame.gameCharacters.size()

            if (remoteCharSize > localCharSize) {
              logger.info(s"Update Char [${remoteGame.id}] ${remoteGame.name} ($localCharSize --> $remoteCharSize)")
            }
          }


          /**
           * upgrade sample img
           */
          val localImgSize = Option(localGame.gameImgs).map(_.size).getOrElse(0)
          val remoteImgSize = Option(remoteGame.gameImgs).map(_.size).getOrElse(0)

          if (remoteImgSize > localImgSize) {

            logger.info(s"Update SampleImage [${RED.s(localGame.id.toString)}] ${RED.s(localGame.name)} ${RED.s(localGame.state.toString)} ($localImgSize --> $remoteImgSize)")
            GameDB.updateImg(remoteGame)
          }

          remoteGame.smallImg = localGame.smallImg

          // check game state

          //State not skip
          if (!GameState.ignoreState().contains(remoteGame.state)) {
            sendTo[PrepareDownloadImageActor](remoteGame)
          } else {
            logger.debug(s"Skip download image ${remoteGame.simpleView}")
          }

        case _ =>
      }
  }

}

private object BrandCache {
  private val brandCache = new SimpleCache[Int, Brand]

  def apply() = brandCache

  def get(brandId: Int) = {
    brandCache.get(brandId).getOrElse {
      val brand = BrandQuery().where(Filters.eq(brandId)).one().orNull
      BrandCache().put(brandId, brand)
      brand
    }
  }
}