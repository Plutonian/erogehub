package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.piplline.handler.DefaultActor
import com.typesafe.scalalogging.Logger

import scala.io.{Codec, Source}
import scala.jdk.CollectionConverters._


/**
 * Check game is new or already has
 */
class PreProcessGameActor extends DefaultActor {

  override def receive = {
    case game: Game =>

      //already has
      if (GameDB.exist(game.id)) {
        logger.debug(s"<Update> ${game.simpleView}")
        GameDB.update(game)
      }
      else {
        //new game
        import PreProcessGameActor._

        //Mark game is spec
        if (isSameGame(game)) {
          game.state = GameState.SAME
        }
        else {
          game.state = GameState.UNCHECKED
        }

        logger.info(s"<Insert> ${game.simpleView} ${game.state}")
        GameDB.insert(game)
      }
      sendTo[DownloadPageActor](game.id)

  }

}

object PreProcessGameActor {
  private val samelist = {
    val source = Source.fromInputStream(getClass.getResourceAsStream("/same.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }

  private val packagelist = {
    val source = Source.fromInputStream(getClass.getResourceAsStream("/package.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }

  def isSameGame = (game: Game) => samelist.exists(str => game.name.contains(str))

  def isPackageGame = (game: Game) =>
    Option(game.`type`).map(_.asScala).getOrElse(List.empty).contains("セット商品") ||
      packagelist.exists(str => game.name.contains(str))
}